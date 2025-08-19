import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
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