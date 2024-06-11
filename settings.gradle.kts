pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven {
            setUrl("https://repo1.maven.org/maven2/")
            setUrl("https://jitpack.io")
            setUrl("https://dl.bintray.com/tencentqcloudterminal/maven")
            setUrl("'https://developer.huawei.com/repo/'")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        jcenter()
        maven {
            setUrl("https://www.jitpack.io")
            setUrl("https://developer.huawei.com/repo/")
            setUrl("https://repo1.maven.org/maven2/")
            setUrl("'https://developer.huawei.com/repo/'")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "TakeATaxiProject"
include(":app")
 