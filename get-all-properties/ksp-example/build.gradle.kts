plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(project(":ksp-processor"))
    ksp(project(":ksp-processor"))
}
