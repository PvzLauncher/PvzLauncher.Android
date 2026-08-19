plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.pvzlauncher.pvzlauncher"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.pvzlauncher.pvzlauncher"
        minSdk = 24
        targetSdk = 29


        //更新时一定要更改此区域
        versionCode = 21
        versionName = "1.2.4"
        //更新时一定要更改此区域

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {

        create("release") {

            storeFile = file("${rootProject.projectDir}/pvzlauncher.jks")

            storePassword = "123456"

            keyAlias = "key0"

            keyPassword = "123456"
        }
    }

    buildTypes {
        release {
            signingConfig =
                signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    lint {
        disable += "ExpiredTargetSdkVersion"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation("com.github.amitshekhariitbhu:PRDownloader:1.0.2")
    implementation("io.github.qbhx224:lint-file:2.1.1")
    implementation("androidx.navigation:navigation-compose:2.9.8")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.7")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.github.jeziellago:compose-markdown:0.5.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3:1.5.0-alpha24")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}