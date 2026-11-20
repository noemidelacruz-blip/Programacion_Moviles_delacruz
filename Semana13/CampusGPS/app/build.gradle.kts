// Importa la clase Properties de Java para poder leer archivos de configuración clave=valor (como local.properties):
import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}
// Lee el archivo local.properties de forma segura sin exponer la clave en el código
val localProps = Properties().apply {
    val archivo = rootProject.file("local.properties")
    if (archivo.exists()) {
        archivo.inputStream().use { load(it) }
    }
}
android {
    namespace = "com.delacruz.campusgps"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        // Inyecta el valor de MAPS_API_KEY en el AndroidManifest.xml al compilar
        manifestPlaceholders["MAPS_API_KEY"] = localProps.getProperty("MAPS_API_KEY", "")
        applicationId = "com.delacruz.campusgps"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
//Las dependencias se ponen para agregar herramientas que nos ayudan a que nuestra aplicación funcione
    // Ubicación, mapa y ViewModel con Compose
    // Librería de Google que lee el chip GPS real del teléfono (FusedLocationProviderClient)(Saca tu ubicación real desde el GPS del celular)
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // Hace que la app espere la respuesta del GPS sin trabarse:
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    // 8.3.1: la 8.4.0 se compiló con Kotlin 2.4 y el compilador del proyecto es 2.2
    // Librería de Google Maps para Jetpack Compose (Muestra el mapa de Google en pantalla (ya trae Google Maps incluido por dentro)
    implementation("com.google.maps.android:maps-compose:8.3.1")
    // Guarda los datos de la pantalla para que no se borren al girar el celular
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    // Íconos de las categorías en la lista (MenuBook, Restaurant, Mic, Star)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}