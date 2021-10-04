// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
//        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}
allprojects {
    repositories {
        mavenCentral()
        maven{
            setUrl("https://jitpack.io")
        }
        google()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}