plugins {
    id(BuildPluginsConfig.androidApplication)
    id(BuildPluginsConfig.androidHilt)
    kotlin(BuildPluginsConfig.kotlinAndroid)
    kotlin(BuildPluginsConfig.kotlinKapt)
    id("kotlin-android")
    `maven-publish`
}

android {
    compileSdk =  (BuildAndroidConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId  = BuildAndroidConfig.APPLICATION_ID
        minSdk  = BuildAndroidConfig.MIN_SDK_VERSION
        targetSdk  = BuildAndroidConfig.TARGET_SDK_VERSION
        versionCode  = BuildAndroidConfig.VERSION_CODE
        versionName  = BuildAndroidConfig.VERSION_NAME

        testInstrumentationRunner  = BuildAndroidConfig.androidTestInstrumentation
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    buildFeatures {
        this.dataBinding = true
    }

    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":base"))
//    api(project(":core"))

    implementation(DependenciesManager.kotlinImplementation)
    implementation(DependenciesManager.lifeCycleKtxImplementation)
    implementation(DependenciesManager.androidxImplementation)
    implementation(DependenciesManager.hiltImplementation)
    kapt(HiltDaggerDependencies.DAGGER_COMPILER)


    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
}