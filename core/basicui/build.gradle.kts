plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.peakmain.ui"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
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
    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.constraintlayout)
    api(libs.material)
    api(libs.okhttp)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.glide)
}
