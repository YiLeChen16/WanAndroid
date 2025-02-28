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
        mavenLocal()
        maven {
            url = uri("https://google.bintray.com/flexbox-layout")
        }
        maven { url = uri("https://repo.grails.org/grails/core/") }

//        maven { url = uri("https://jitpack.io") }

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

rootProject.name = "WanAndroid"
include(":app")
