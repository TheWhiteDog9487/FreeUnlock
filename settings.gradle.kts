pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*") } }
        mavenCentral()
        gradlePluginPortal() } }

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
