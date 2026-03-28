// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.cheatshqip.sonar.report)
    // SonarQube Gradle plugin disabled until AGP 9 support lands in plugin 7.3 (April 2026).
    // alias(libs.plugins.sonarqube)
}
