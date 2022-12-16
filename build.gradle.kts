import java.util.Properties
import java.io.FileInputStream

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "de.skycave.shoprotation"
version = "1.0.0"

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }

    maven {
        url = uri("https://maven.pkg.github.com/skycavemc/skycavelib")
        credentials {
            username = localProperties.getProperty("gpr.user")
            password = localProperties.getProperty("gpr.key")
        }
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("de.skycave:skycavelib:1.4.1")
    compileOnly("org.mongodb:mongodb-driver-sync:4.7.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.heuerleon:mcguiapi:v1.3.5")
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

