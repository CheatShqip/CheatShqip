@file:Suppress("PackageName")

package com.cheatshqip.buildlogic.detekt

import io.gitlab.arturbosch.detekt.api.AnnotationExcluder
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.DetektVisitor
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtModifierListOwner
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.PropertyAccessorDescriptor
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall

@Suppress("ViolatesTypeResolutionRequirements")
class UnusedPublicMember(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        "UnusedPublicMember",
        Severity.Maintainability,
        "Public member is unused within this module.",
        Debt.FIVE_MINS,
    )

    private val allowedNames: Regex by config("", String::toRegex)

    private val ignoreAnnotated: List<String> by config(
        listOf("Composable", "Serializable", "Keep", "Module", "KoinComponent"),
    )

    override fun visit(root: KtFile) {
        super.visit(root)
        if (bindingContext == BindingContext.EMPTY) return
        val excluder = AnnotationExcluder(
            root,
            ignoreAnnotated.map { it.replace(".", "\\.").replace("*", ".*").toRegex() },
            bindingContext,
        )
        val visitor = UnusedPublicMemberVisitor(allowedNames, excluder, bindingContext)
        root.accept(visitor)
        visitor.getUnusedReports(issue).forEach { report(it) }
    }
}

private class UnusedPublicMemberVisitor(
    private val allowedNames: Regex,
    private val annotationExcluder: AnnotationExcluder,
    private val bindingContext: BindingContext,
) : DetektVisitor() {
    private val declarations = mutableListOf<KtNamedDeclaration>()

    private val referencedDescriptors by lazy {
        fun unwrap(descriptor: org.jetbrains.kotlin.descriptors.DeclarationDescriptor) =
            when (descriptor) {
                is PropertyAccessorDescriptor ->
                    listOf(descriptor.original, descriptor.correspondingProperty.original)
                is ConstructorDescriptor ->
                    listOf(descriptor.original, descriptor.containingDeclaration.original)
                else ->
                    listOf(descriptor.original)
            }

        val fromReferenceTarget = bindingContext
            .getSliceContents(BindingContext.REFERENCE_TARGET)
            .values
            .flatMap { unwrap(it) }

        // Property reads are also stored as resolved getter calls — cover both slices.
        val fromResolvedCalls = bindingContext
            .getSliceContents(BindingContext.RESOLVED_CALL)
            .values
            .flatMap { unwrap(it.resultingDescriptor) }

        (fromReferenceTarget + fromResolvedCalls).toSet()
    }

    fun getUnusedReports(issue: Issue): List<CodeSmell> =
        declarations.mapNotNull { declaration ->
            val descriptor = bindingContext[BindingContext.DECLARATION_TO_DESCRIPTOR, declaration]
                ?: return@mapNotNull null
            if (descriptor.original !in referencedDescriptors) {
                CodeSmell(
                    issue,
                    Entity.atName(declaration),
                    "Public member `${declaration.nameAsSafeName.identifier}` is unused.",
                )
            } else {
                null
            }
        }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (function.isLocal) return
        if (shouldSkip(function)) return
        if (function.getStrictParentOfType<KtClass>()?.isInterface() == true) return
        if (function.hasModifier(KtTokens.OVERRIDE_KEYWORD)) return
        if (function.hasModifier(KtTokens.OPERATOR_KEYWORD)) return
        maybeAdd(function)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (property.isLocal) return
        if (isConstructorPropertyParameter(property)) return
        if (shouldSkip(property)) return
        if (property.hasModifier(KtTokens.OVERRIDE_KEYWORD)) return
        maybeAdd(property)
    }

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)
        if (classOrObject.name == null) return // anonymous object
        if (!isTopLevelOrMember(classOrObject)) return // local class
        if (classOrObject is KtObjectDeclaration && classOrObject.isCompanion()) return
        if (shouldSkip(classOrObject)) return
        maybeAdd(classOrObject)
    }

    // A class/object is top-level or a member if its parent is a file or a class body.
    // Anything else (e.g. a local class inside a function) is considered local.
    private fun isTopLevelOrMember(classOrObject: KtClassOrObject): Boolean =
        classOrObject.parent is KtFile || classOrObject.parent is KtClassBody

    // Primary constructor val/var parameters are visited as KtProperty nodes by the Kotlin PSI.
    // Their REFERENCE_TARGET resolves differently than DECLARATION_TO_DESCRIPTOR yields,
    // causing false positives. Skip them here and rely on actual usages being caught via the
    // containing class instead.
    private fun isConstructorPropertyParameter(property: KtProperty): Boolean {
        var parent = property.parent
        while (parent != null) {
            if (parent is KtPrimaryConstructor) return true
            if (parent is KtClassBody || parent is KtFile) return false
            parent = parent.parent
        }
        return false
    }

    private fun maybeAdd(declaration: KtNamedDeclaration) {
        if (!allowedNames.matches(declaration.nameAsSafeName.identifier)) {
            declarations.add(declaration)
        }
    }

    private fun shouldSkip(declaration: KtNamedDeclaration): Boolean {
        if (!declaration.isEffectivelyPublic()) return true
        if (annotationExcluder.shouldExclude(declaration.annotationEntries)) return true
        if (declaration.hasModifier(KtTokens.EXPECT_KEYWORD)) return true
        if (declaration.hasModifier(KtTokens.ACTUAL_KEYWORD)) return true
        return false
    }

    private fun KtModifierListOwner.isEffectivelyPublic(): Boolean =
        !hasModifier(KtTokens.PRIVATE_KEYWORD) &&
            !hasModifier(KtTokens.PROTECTED_KEYWORD) &&
            !hasModifier(KtTokens.INTERNAL_KEYWORD)
}
