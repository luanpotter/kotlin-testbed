plugins {
    kotlin("jvm") version "1.9.22"
}

group = "xyz.luan.test"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(21)
}