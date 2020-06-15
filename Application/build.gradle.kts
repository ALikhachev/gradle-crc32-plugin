group = "com.alikhachev.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val includedLibs by configurations.creating

dependencies {
    includedLibs("org.apache.commons:commons-lang3:3.10")
}

plugins {
    java
    id("com.alikhachev.jetbrains.crc32") version ("1.0-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    from(includedLibs.map { if (it.isDirectory()) it else zipTree(it) })
}