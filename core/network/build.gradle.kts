plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.sys.androidkit.core.network"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(":core:common"))
    api(libs.retrofit)
    api(libs.retrofit.converter.moshi)
    api(libs.moshi.kotlin)
    api(libs.okhttp)
    api(libs.okhttp.logging)
}
