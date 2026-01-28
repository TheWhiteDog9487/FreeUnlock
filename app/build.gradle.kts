import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)

    alias(libs.plugins.ksp)
}

android {
    namespace = "xyz.thewhitedog9487.freeunlock"
    compileSdk {
        version = release(36) }

    defaultConfig {
        applicationId = "xyz.thewhitedog9487.freeunlock"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true }

    signingConfigs {
        val KeystorePath = System.getProperty("Android_Keystore_Path")
        if( KeystorePath == null || file(KeystorePath).exists() == false ) {
            return@signingConfigs }

        create("release"){
            storeFile = file(KeystorePath)
            storePassword = System.getProperty("Android_Keystore_Password")
            keyAlias = System.getProperty("Android_Keystore_Alias")
            keyPassword = System.getProperty("Android_Keystore_Alias_Password")

            if ( arrayOf<Any?>( storePassword, keyAlias, keyPassword ).all { it != null } == false ) {
                throw RuntimeException("签名配置错误，缺少必要的环境变量。") } }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(libs.androidx.annotation)

    // 基础依赖
    implementation(libs.yukihookapi.api)
    // 推荐使用 KavaRef 作为核心反射 API
    implementation(libs.kavaref.core)
    implementation(libs.kavaref.extension)
    // 作为 Xposed 模块使用务必添加，其它情况可选
    compileOnly(libs.xposed.api)
    // 作为 Xposed 模块使用务必添加，其它情况可选
    ksp(libs.yukihookapi.ksp.xposed)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17 } }

