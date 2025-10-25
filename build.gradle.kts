plugins {
    val kotlinVersion = "1.9.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "cn.chahuyun"
version = "1.0.0"

repositories {
    maven("https://nexus.jsdu.cn/repository/maven-public/")
    mavenCentral()
}

dependencies {
    implementation("cn.chahuyun:hibernate-plus:1.0.17")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}

// hibernate 6 和 HikariCP 5 需要 jdk11
mirai {
    jvmTarget = JavaVersion.VERSION_17
}



