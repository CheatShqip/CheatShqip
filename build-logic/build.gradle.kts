import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
    detektPlugins(libs.detekt.formatting)
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
        register("sonarReport") {
            id = "com.cheatshqip.sonar.report"
            implementationClass = "com.cheatshqip.buildlogic.SonarReportPlugin"
        }
    }
}
