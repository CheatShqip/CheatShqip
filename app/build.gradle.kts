plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.cheatshqip"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cheatshqip"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "0.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "API_BASE_URL",
            value = "\"https://qub10cxllf.execute-api.eu-central-1.amazonaws.com/prod/\""
        )
    }

    flavorDimensions += "environment"
    productFlavors {
        create("prod") { dimension = "environment" }
        create("mock") { dimension = "environment" }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("analyze") {
            isMinifyEnabled = true
            isDebuggable = false
            matchingFallbacks += listOf("release")
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
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
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
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektMain") {
    description = "Run detekt with type resolution for main sources"
    buildUponDefaultConfig = true
    allRules = true
    autoCorrect = false
    config.setFrom("$rootDir/detekt-config.yml")
    setSource(files("src/main/java"))
    classpath.setFrom(
        tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileMockDebugKotlin")
            .map { it.libraries },
    )
    include("**/*.kt")
}

dependencies {
    implementation(project(":tosk"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.mlkit.translate)
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.okhttp3.bom))
    testImplementation(libs.junit.juniper)
    testImplementation(libs.junit.juniper.params)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.koin.test)
    testImplementation(libs.okhttp3.mockwebserver)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.okhttp3.bom))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.okhttp3.mockwebserver)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    detektPlugins(libs.detekt.formatting)
}
