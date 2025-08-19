import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
}

gradlePlugin {
    plugins {
        register("moduleAndroidPresentation") {
            id = "com.cheatshqip.module.android.presentation"
            implementationClass = "com.cheatshqip.build_logic.AndroidPresentationModulePlugin"
        }
    }
}