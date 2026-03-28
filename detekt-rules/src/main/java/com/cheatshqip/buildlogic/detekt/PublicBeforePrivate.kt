@file:Suppress("PackageName")

package com.cheatshqip.buildlogic.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtModifierListOwner
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty

/**
 * Enforces declaration ordering:
 *
 * File level:
 *   1. public const
 *   2. private const
 *   3. public class / function / property
 *   4. private class / function / property
 *   5. @Preview functions
 *
 * Class level:
 *   1. public const
 *   2. private const
 *   3. public properties (+ private backing properties prefixed with `_`)
 *   4. private properties
 *   5. abstract members
 *   6. override members
 *   7. public methods
 *   8. private methods
 *   9. companion object
 */
class PublicBeforePrivate(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        "PublicBeforePrivate",
        Severity.Style,
        "Declaration is out of order: public const → private const → public property → private property → abstract → override → public function → private function → companion.",
        Debt.FIVE_MINS,
    )

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        checkOrdering(file.declarations, ::fileRank)
    }

    override fun visitClassBody(classBody: KtClassBody) {
        super.visitClassBody(classBody)
        checkOrdering(classBody.declarations, ::classRank)
    }

    private fun checkOrdering(declarations: List<KtDeclaration>, rankFn: (KtDeclaration) -> Int) {
        var maxRank = 0
        declarations.forEach { decl ->
            val rank = rankFn(decl)
            if (rank < maxRank) {
                report(
                    CodeSmell(
                        issue,
                        Entity.atName(decl as KtNamedDeclaration),
                        "'${(decl as KtNamedDeclaration).name}' is out of order.",
                    )
                )
            }
            maxRank = maxOf(maxRank, rank)
        }
    }

    private fun fileRank(decl: KtDeclaration): Int {
        val isPrivate = (decl as? KtModifierListOwner)?.hasModifier(KtTokens.PRIVATE_KEYWORD) == true
        val isConst = decl is KtProperty && decl.hasModifier(KtTokens.CONST_KEYWORD)
        val isPreview = (decl as? KtAnnotated)?.annotationEntries
            ?.any { entry -> entry.shortName?.asString()?.contains("Preview") == true } == true
        return when {
            isPreview -> 4
            isConst && !isPrivate -> 0
            isConst && isPrivate -> 1
            !isPrivate -> 2
            else -> 3
        }
    }

    private fun classRank(decl: KtDeclaration): Int {
        val isPrivate = (decl as? KtModifierListOwner)?.hasModifier(KtTokens.PRIVATE_KEYWORD) == true
        val isConst = decl is KtProperty && decl.hasModifier(KtTokens.CONST_KEYWORD)
        val isAbstract = (decl as? KtModifierListOwner)?.hasModifier(KtTokens.ABSTRACT_KEYWORD) == true
        val isOverride = (decl as? KtModifierListOwner)?.hasModifier(KtTokens.OVERRIDE_KEYWORD) == true
        val isCompanion = decl is KtObjectDeclaration && decl.isCompanion()
        val isProperty = decl is KtProperty
        val isFunction = decl is KtNamedFunction
        val isBacking = isProperty && isPrivate && (decl as? KtNamedDeclaration)?.name?.startsWith("_") == true
        return when {
            isConst && !isPrivate -> 0
            isConst && isPrivate -> 1
            isAbstract -> 4
            isOverride -> 5
            isCompanion -> 8
            isProperty && (!isPrivate || isBacking) -> 2
            isProperty && isPrivate -> 3
            isFunction && !isPrivate -> 6
            isFunction && isPrivate -> 7
            !isPrivate -> 6
            else -> 7
        }
    }
}