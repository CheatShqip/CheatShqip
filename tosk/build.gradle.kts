plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.cheatshqip.module.android.presentation)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cheatshqip.realisation.tosk"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api")
        }
    }
}

dependencies {
}