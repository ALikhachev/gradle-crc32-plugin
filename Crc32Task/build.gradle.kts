plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
    `java-gradle-plugin`
}

group = "com.alikhachev.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation("org.apache.commons:commons-compress:1.20")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("crc32") {
            from(components["java"])
        }
    }
}

gradlePlugin {
    plugins {
        create("crc32Plugin") {
            id = "com.alikhachev.jetbrains.crc32"
            implementationClass = "com.alikhachev.jetbrains.crc32.Crc32Plugin"
        }
    }
}