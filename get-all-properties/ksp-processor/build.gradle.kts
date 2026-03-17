plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.20-2.0.4")
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")

    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")
}
