import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android) }

android {
    namespace = "xyz.thewhitedog9487.freeunlock"
    compileSdk {
        version = release(37) }

    defaultConfig {
        applicationId = "xyz.thewhitedog9487.freeunlock"
        minSdk = 26
        targetSdk = 37
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }

    signingConfigs {
        val KeystorePath = System.getenv("Android_Keystore_Path")
            ?.takeIf { it.isNotBlank() && file(it).exists() }
        if( KeystorePath == null ) {
            println("[警告] Android_Keystore_Path 无效")
            create("release")
            return@signingConfigs }

        create("release"){
            storeFile = file(KeystorePath)
            storePassword = System.getenv("Android_Keystore_Password")
            keyAlias = System.getenv("Android_Keystore_Alias")
            keyPassword = System.getenv("Android_Keystore_Alias_Password")

            if ( arrayOf<Any?>( storePassword, keyAlias, keyPassword ).all { it != null } == false ) {
                throw RuntimeException("签名配置错误，缺少必要的环境变量。") } } }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro") } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17 }

    buildFeatures {
        buildConfig = true } }

base {
    archivesName.set("FreeUnlock-${android.defaultConfig.versionName}-${android.defaultConfig.versionCode}") }

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(libs.androidx.annotation)

    platform(libs.yukihook.bom).apply {
        implementation(this)
        ksp(this) }

    implementation(libs.yukihook.core)
    implementation(libs.yukihook.extension.kavaref)
    implementation(libs.yukihook.runtime.libxposed)
//    implementation(libs.yukihook.runtime.xposed82)
//    compileOnly(libs.xposed.framework.api)
    ksp(libs.yukihook.compiler)
    compileOnly(libs.libxposed.api)
    implementation(platform(libs.kavaref.bom))
    implementation(libs.kavaref.core)
    implementation(libs.kavaref.android)
    implementation(libs.kavaref.extension) }

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17 } }