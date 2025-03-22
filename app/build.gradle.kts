plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.znextapp.interactivetaskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.znextapp.interactivetaskmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    defaultConfig {
        testInstrumentationRunner = "dagger.hilt.android.testing.HiltTestRunner"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.androidx.navigation.testing)
    val room_version = "2.6.1"
    val hilt_version = "2.51.1"

    // Core Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)

    // Compose Material
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation("androidx.compose.material3:material3:1.2.0")

    // Room Database
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Hilt Dependency Injection
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")

    // Utility
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")
    implementation("com.google.guava:guava:31.0.1-android")

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose Testing
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Fix Espresso Conflict
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Hilt Testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$hilt_version")

    // Robolectric for Unit Tests
    androidTestImplementation("org.robolectric:robolectric:4.9")
    androidTestImplementation ("com.karumi:shot-android:5.15.2")

    // Debug Dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
