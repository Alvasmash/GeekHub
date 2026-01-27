plugins {
    // Estos alias están bien, los mantenemos
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.example.geekhub"
    compileSdk = 34 // Usa 34, ya que 36 es una versión preview y puede ser inestable.

    defaultConfig {
        applicationId = "com.example.geekhub"
        minSdk = 24
        targetSdk = 34 // Coherente con compileSdk
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
        sourceCompatibility = JavaVersion.VERSION_1_8 // Estándar recomendado
        targetCompatibility = JavaVersion.VERSION_1_8 // Estándar recomendado
    }
    kotlinOptions {
        jvmTarget = "1.8" // Estándar recomendado
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Versión estable para el compilador de Compose
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// === SECCIÓN DE DEPENDENCIAS COMPLETAMENTE LIMPIA Y ORDENADA ===
dependencies {

    // Retrofit para las peticiones de red
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// ViewModel y Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")


    // --- Core y Lifecycle ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(libs.ads.mobile.sdk)

    // --- Jetpack Compose (Usando el BOM para gestionar versiones) ---
    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // --- Navegación con Compose (Crucial para conectar pantallas) ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- Geolocalización de Google ---
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // --- Dependencias de Testing (sin cambios) ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
