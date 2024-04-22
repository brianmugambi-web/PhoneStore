
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.dukastore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dukastore"
        minSdk = 24
        targetSdk = 34
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
    buildTypes {
        forEach {
            it.buildConfigField(
                "String",
                "CONSUMER_KEY",
                "\"${rootProject.findProperty("DARAJA_CONSUMER_KEY") ?: ""}\""
            )
            it.buildConfigField(
                "String",
                "CONSUMER_SECRET",
                "\"${rootProject.findProperty("DARAJA_CONSUMER_SECRET") ?: ""}\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig =true
    }



}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("com.github.dangiashish:Auto-Image-Slider:1.0.4")
    // mpesa dependencies

    // circle imageviewer
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //paypal
    implementation ("com.paypal.sdk:paypal-android-sdk:2.14.2")
    implementation("com.paypal.checkout:android-sdk:1.2.1")

    //notification badge
    implementation ("com.nex3z:notification-badge:1.0.4")
    implementation("org.greenrobot:eventbus:3.3.1")



}



