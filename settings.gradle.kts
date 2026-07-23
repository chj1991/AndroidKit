pluginManagement {
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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "AndroidKit"

include(":app")

include(":core:common")
include(":core:ui")
include(":core:datastore")
include(":core:database")
include(":core:network")
include(":core:basicui")
include(":core:androidktx")

include(":feature:home")
include(":feature:settings")
include(":feature:sample-counter")
include(":feature:lifecycle")
include(":feature:async")
include(":feature:recycler")
include(":feature:storage")
include(":feature:network")
include(":feature:system")
include(":feature:view-custom")
include(":feature:components")
include(":feature:basic-ui")
include(":feature:android-ktx")
include(":feature:charts")
include(":feature:charts-custom")
include(":feature:animation")
include(":feature:image")
include(":feature:performance")
include(":feature:compat")

include(":benchmark")
