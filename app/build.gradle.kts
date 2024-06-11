@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.example.takeataxiproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.takeataxiproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("config") { // 签名配置的名字，可以随意起，比如myconfig等
            keyAlias = "car123456" // 签名别名
            keyPassword = "car123456" // 别名对应的密码
            storeFile = file("D:/keystory/car.jks") // 正式签名文件位置
            storePassword = "car123456" // 签名密码
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packagingOptions {
        // pickFirsts:当出现重复文件，会使用第一个匹配的文件打包进入apk
        pickFirst ("assets/location_map_gps_locked.png")
        pickFirst ("assets/location_map_gps_3d.png")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)



    implementation(libs.utilcode)
    implementation(libs.baseRecyclerViewAdapterHelper)

//        implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
//        implementation ('androidx.core:core-ktx:1.3.2')

    val latest_version = "2.5.1"
    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:$latest_version")
    // optional - 支持Rxjava2与Room
    implementation("androidx.room:room-rxjava2:$latest_version")

    // optional - 支持协程与Room
    implementation("androidx.room:room-ktx:$latest_version")
    implementation("org.litepal.guolindev:core:3.2.3")
    implementation(files("libs/AMap2DMap_6.0.0_AMapSearch_9.7.1_AMapLocation_6.4.3_20240314.jar"))
    implementation ("com.amap.api:navi-3dmap:7.4.0_3dmap7.4.0")
    implementation("com.guolindev.permissionx:permissionx:1.7.1")
    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation ("com.google.android.material:material:1.2.1")
    implementation  ("com.afollestad.material-dialogs:commons:0.9.6.0")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

}