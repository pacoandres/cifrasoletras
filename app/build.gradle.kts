plugins {
    id("com.android.application")
}

android {
    namespace = "org.gnu.itsmoroto.cifrasoletras"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.gnu.itsmoroto.cifrasoletras"
        minSdk = 24
        targetSdk = 35
        //Use 0 as . in version code.
        versionCode = 10300
        versionName = "1.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    tasks.withType<JavaCompile>(){
        options.compilerArgs.addAll(listOf("-nowarn", "-Xlint:deprecation"))
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }


}

dependencies {

    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
