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
        versionCode = 10202
        versionName = "1.2.2"

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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}