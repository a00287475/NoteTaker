plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
//    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.notetaker.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.notetaker.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets { // 🔹 Added explicit source set for google-services.json
        getByName("main") {
            res.srcDirs("src/main/res")
            assets.srcDirs("src/main/assets")
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.ui.database)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))


}




allprojects {
    repositories {
        google() // <-- This is required
        mavenCentral()
    }
}