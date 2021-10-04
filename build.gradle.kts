// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(BuildClassesConfig.ANDROID_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.KOTLIN_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.HILT_GRADLE_PLUGIN)
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
        maven {
            setUrl("https://raw.github.com/synergian/wagon-git/releases") }
        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/mb28/android-yap-permissions/src/releasesLib")
        }
        google()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}