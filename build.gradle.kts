plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.allopen") version "1.5.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.3.1"
}

group = "me.bardy"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("it.unimi.dsi", "fastutil", "8.5.4")
    implementation("com.google.guava", "guava", "30.1.1-jre")
    implementation("org.spongepowered", "math", "2.0.0")
}
