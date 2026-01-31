plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Ensure 'kotlin-kapt' or 'ksp' is REMOVED unless you need it for other libraries
}

android {
    namespace = "com.example.neurotrack" // Ensure this matches your package name
    compileSdk = 36 // Use the latest stable SDK

    defaultConfig {
        applicationId = "com.example.neurotrack"
        minSdk = 24 // A common minimum SDK target
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // 💡 Essential for accessing UI elements easily (ViewBinding)
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // ----------------------------------------------------
    // Android Standard Libraries
    // ----------------------------------------------------
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ----------------------------------------------------
    // Google Material Design (for UI components)
    // ----------------------------------------------------
    implementation("com.google.android.material:material:1.9.0")

    // ----------------------------------------------------
    // Lifecycle, ViewModel, and Coroutines (Modern Android Architecture)
    // ----------------------------------------------------
    // ViewModel and LiveData KTX
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // KTX for easier Activity access
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation(libs.androidx.activity)

    // ----------------------------------------------------
    // Testing Dependencies
    // ----------------------------------------------------
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.airbnb.android:lottie:6.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.airbnb.android:lottie:6.4.0")
}