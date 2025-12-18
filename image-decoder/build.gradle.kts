plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "tachiyomi.decoder"
    compileSdk = 36
    buildToolsVersion = "35.0.1"
    ndkVersion = "27.0.12077973"

    defaultConfig {
        minSdk = 26

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }

        // Build native lib (libimagedecoder.so)
        externalNativeBuild {
            cmake {
                // 16 KB page-size compatibility for Android 15+ (Armv9)
                arguments += listOf(
                    "-DANDROID_STL=c++_static",
                    "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON",

                    // Fix Windows CMake failing when libwebp tries to generate install/export files
                    "-DCMAKE_SKIP_INSTALL_RULES=TRUE",
                    "-DCMAKE_INSTALL_DATAROOTDIR=share",
                    "-DCMAKE_INSTALL_DATADIR=share",
					"-DWITH_AVIF=ON",
					"-DWITH_HEIF=ON",
                )
            }
        }

        consumerProguardFiles("consumer-rules.pro")
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // This makes publishing work cleanly for Android libraries
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}
