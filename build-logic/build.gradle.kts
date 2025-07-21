import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)

    gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
        compileOnly(files(it.absolutePath))
    }
}

group = "com.cheatshqip.build-logic"

gradlePlugin {
    plugins {
        register("moduleAndroidPresentation") {
            id = "com.cheatshqip.module.android.presentation"
            implementationClass = "com.cheatshqip.build_logic.AndroidPresentationModulePlugin"
        }
    }
}