import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
    compileOnly(libs.detekt.api)
    detektPlugins(libs.detekt.formatting)
   // detektPlugins(files("build/libs/build-logic.jar"))
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    autoCorrect = false
    config.setFrom("${rootDir.parentFile}/detekt-config.yml")
}

gradlePlugin {
    plugins {
        register("moduleAndroidPresentation") {
            id = "com.cheatshqip.module.android.presentation"
            implementationClass = "com.cheatshqip.buildlogic.AndroidPresentationModulePlugin"
        }
    }
}
