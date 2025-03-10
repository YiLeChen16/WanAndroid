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
        /* maven {
             url = uri("https://google.bintray.com/flexbox-layout")
         }*/

        // JitPack 远程仓库：https://jitpack.io
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.grails.org/grails/core/") }


        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    /*repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)： 这是一个至关重要的设置。
    它强制要求依赖项必须从这个 dependencyResolutionManagement 代码块中定义的仓库解析。
    如果在此仓库中找不到依赖项， 构建将失败。
    这可以防止意外使用在各个模块级 build.gradle 文件中定义的仓库中的依赖项。*/
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // JitPack 远程仓库：https://jitpack.io
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.grails.org/grails/core/") }
    }
}

rootProject.name = "WanAndroid"
include(":app")
