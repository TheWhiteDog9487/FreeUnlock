pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*") } }
        mavenCentral()
        gradlePluginPortal() } }

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases")
        maven("https://api.xposed.info/") {
            content {
                includeGroup("de.robv.android.xposed") } } } }

rootProject.name = "FreeUnlock"
include(":app")
