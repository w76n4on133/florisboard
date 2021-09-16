plugins {
    id("com.android.application") version "7.0.2"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"
    ndkVersion = "22.1.7171670"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.contracts.ExperimentalContracts")
    }

    defaultConfig {
        applicationId = "dev.patrickgold.florisboard"
        minSdk = 23
        targetSdk = 30
        versionCode = 57
        versionName = "0.3.14"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    Pair("room.schemaLocation", "$projectDir/schemas"),
                    Pair("room.incremental", "true"),
                    Pair("room.expandProjection", "true")
                )
            }
        }

        externalNativeBuild {
            cmake {
                cFlags("-fvisibility=hidden", "-DU_STATIC_IMPLEMENTATION=1")
                cppFlags("-fvisibility=hidden", "-std=c++17", "-fexceptions", "-ffunction-sections", "-fdata-sections", "-DU_DISABLE_RENAMING=1", "-DU_STATIC_IMPLEMENTATION=1")
                arguments("-DANDROID_STL=c++_static")
            }
        }

        ndk {
            //abiFilters += listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        sourceSets {
            maybeCreate("main").apply {
                assets {
                    srcDirs("src/main/assets", "src/main/icu4c/prebuilt/assets")
                }
                jniLibs {
                    srcDirs("src/main/icu4c/prebuilt/jniLibs")
                }
            }
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha04"
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        named("debug").configure {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            isDebuggable = true
            isJniDebuggable = true

            resValue("mipmap", "floris_app_icon", "@mipmap/ic_app_icon_debug")
            resValue("mipmap", "floris_app_icon_round", "@mipmap/ic_app_icon_debug_round")
            resValue("string", "floris_app_name", "FlorisBoard Debug")
        }

        create("beta") // Needed because by default the "beta" BuildType does not exist
        named("beta").configure {
            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta01"
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))

            resValue("mipmap", "floris_app_icon", "@mipmap/ic_app_icon_beta")
            resValue("mipmap", "floris_app_icon_round", "@mipmap/ic_app_icon_beta_round")
            resValue("string", "floris_app_name", "FlorisBoard Beta")
        }

        named("release").configure {
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))

            resValue("mipmap", "floris_app_icon", "@mipmap/ic_app_icon_release")
            resValue("mipmap", "floris_app_icon_round", "@mipmap/ic_app_icon_release_round")
            resValue("string", "floris_app_name", "@string/app_name")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lint {
        isAbortOnError = false
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.activity:activity-ktx:1.3.1") // possibly remove after settings rework
    implementation("androidx.appcompat:appcompat:1.3.1") // possibly remove after settings rework
    implementation("androidx.autofill:autofill:1.1.0")
    implementation("androidx.compose.material:material:1.1.0-alpha04")
    implementation("androidx.compose.ui:ui:1.1.0-alpha04")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.0-alpha04")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0") // possibly remove after settings rework
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6") // possibly remove after settings rework
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha08")
    implementation("androidx.preference:preference-ktx:1.1.1") // possibly remove after settings rework
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.18.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0") // possibly remove after settings rework
    implementation("com.google.android.material:material:1.4.0") // possibly remove after settings rework
    implementation("com.jakewharton.timber:timber:4.7.1") // possibly remove after settings rework
    implementation("com.jaredrummler:colorpicker:1.1.0") // possibly remove after settings rework
    implementation("com.nambimobile.widgets:expandable-fab:1.0.2") // possibly remove after settings rework
    implementation("dev.patrickgold.jetpref:jetpref-datastore-model:0.1.0-alpha05")
    implementation("dev.patrickgold.jetpref:jetpref-ui-compose:0.1.0-alpha05")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

    testImplementation(kotlin("test"))
    testImplementation("androidx.test", "core", "1.3.0")
    testImplementation("org.mockito", "mockito-inline", "3.7.7")
    testImplementation("org.robolectric", "robolectric", "4.5.1")

    androidTestImplementation("androidx.test.ext", "junit", "1.1.2")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.3.0")
}
