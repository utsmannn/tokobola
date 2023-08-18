import org.gradle.internal.impldep.org.jsoup.safety.Safelist.basic
rootProject.name = "TokoBola"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)
        id("dev.icerock.mobile.multiplatform-resources").version("0.23.0")
        id("com.codingfeline.buildkonfig").version("0.13.3")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                // Use the secret token you stored in gradle.properties as the password
                password = "sk.eyJ1Ijoia3VjaW5nYXBlcyIsImEiOiJjbGxmdnVtbGIwemdqM2txaHZtam5teGxiIn0.4AlEjvqBWJgaYOElmi5bxA"
            }
        }
    }
}

include(":androidApp")
include(":shared")
include(":resources")
include(":libraries:core")
include(":libraries:common")
include(":libraries:network")
include(":libraries:api")
include(":libraries:database")
include(":libraries:location")
include(":features:home")
include(":features:details")
include(":features:explore")
include(":features:wishlist")
include(":features:cart")
