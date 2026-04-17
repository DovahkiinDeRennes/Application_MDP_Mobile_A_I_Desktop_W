import org.jetbrains.compose.desktop.application.dsl.TargetFormat
val composeVersion = "1.6.11"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    id("app.cash.sqldelight") version "2.0.2"
}

kotlin {

    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        // -------------------------
        // COMMON
        // -------------------------
        commonMain.dependencies {

            // ❌ NE PAS déclarer runtime/foundation/ui manuellement

            // ✅ UNE SEULE entrée Compose Desktop gère tout
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            // Icons (JetBrains compatible)
            implementation("org.jetbrains.compose.material:material-icons-extended:$composeVersion")

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        // -------------------------
        // ANDROID
        // -------------------------
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.uiToolingPreview)

            implementation("app.cash.sqldelight:android-driver:2.0.2")
            implementation("androidx.datastore:datastore-preferences:1.1.1")

            // ❌ IMPORTANT : garde navigation Android ici uniquement
            implementation("androidx.navigation:navigation-compose:2.9.7")
        }

        // -------------------------
        // DESKTOP (JVM)
        // -------------------------
        jvmMain.dependencies {

            // ❌ NE PAS utiliser desktop-jvm + runtime manuel en même temps
            implementation(compose.desktop.currentOs)

            implementation(libs.kotlinx.coroutinesSwing)

            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
            implementation("org.mindrot:jbcrypt:0.4")

            // ❌ SUPPRIME : navigation-compose JVM stub (cause crash)
            // implementation(libs.androidx.navigation.compose.jvmstubs)
        }
    }
}

android {
    namespace = "com.example.demo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.demo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.example.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)

            packageName = "com.example.demo"
            packageVersion = "1.0.0"

            includeAllModules = true

            javaHome = System.getProperty("java.home")
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.demo.db")
        }
    }
}