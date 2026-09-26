plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.cheatshqip"
    compileSdk = 37

    signingConfigs {
        create("release") {
            val ksPath = project.findProperty("storeFile") as? String
                ?: System.getenv("KEYSTORE_PATH")
            val ksPass = project.findProperty("storePassword") as? String
                ?: System.getenv("KEYSTORE_PASSWORD")
            val kAlias = project.findProperty("keyAlias") as? String
                ?: System.getenv("KEY_ALIAS")
            val kPass = project.findProperty("keyPassword") as? String
                ?: System.getenv("KEY_PASSWORD")

            if (ksPath != null && ksPass != null && kAlias != null && kPass != null) {
                storeFile = file(ksPath)
                storePassword = ksPass
                keyAlias = kAlias
                keyPassword = kPass
            }
        }
    }

    defaultConfig {
        applicationId = "com.cheatshqip"
        minSdk = 24
        targetSdk = 37
        versionCode = 6
        versionName = "0.0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("analyze") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            matchingFallbacks += listOf("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    autoCorrect = false
    config.setFrom("$rootDir/detekt-config.yml")
    source.setFrom(
        "src/main/java",
        "src/test/java",
        "src/androidTest/java",
    )
}

dependencies {
    implementation(project(":tosk"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    implementation(libs.google.play.app.update)
    implementation(libs.google.play.app.update.ktx)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.sqlite.bundled)
    ksp(libs.androidx.room.compiler)

    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit.juniper)
    testImplementation(libs.junit.juniper.params)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.koin.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    detektPlugins(libs.detekt.formatting)
    detektPlugins(project(":detekt-rules"))
}
