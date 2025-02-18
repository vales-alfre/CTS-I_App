plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility =JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "17"
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.media3.common.ktx)
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("com.mapbox.maps:android:10.13.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.foundation:foundation:1.6.1")
    implementation("com.google.accompanist:accompanist-pager:0.31.1-alpha")
    implementation("androidx.compose.ui:ui:1.6.1")
    implementation ("androidx.compose.ui:ui-tooling:1.6.1")
    implementation ("androidx.compose.ui:ui-util:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.compose.material3:material3:1.1.2")
    implementation ("com.kizitonwose.calendar:compose:2.4.0")
    implementation ("androidx.compose.material:material-icons-extended:1.6.1")
    implementation ("androidx.compose.ui:ui-graphics:1.6.1")
    implementation ("androidx.compose.material:material-icons-extended:1.6.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation(libs.androidx.material3)
    implementation(libs.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}