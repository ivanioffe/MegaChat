pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MegaChat"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:auth")
include(":core:common")
include(":core:designsystem")
include(":core:domain:base")
include(":core:firebase")
include(":core:mvu")
include(":core:presentation")
include(":core:screenshot-testing")
include(":core:testing")
include(":core:ui")

include(":feature:auth")
include(":feature:onboarding")
