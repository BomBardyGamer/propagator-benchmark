plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("it.unimi.dsi", "fastutil", "8.5.4")
    implementation("com.google.guava", "guava", "30.1.1-jre")
}
