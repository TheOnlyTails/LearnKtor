import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.21"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.4.21"
	id("application")
}

val kotlinVersion: String by extra
val ktorVersion: String by extra
val logbackVersion: String by extra
val kotlinSerialization: String by extra
val exposedVersion: String by extra
val h2Version: String by extra

group = "com.theonlytails.learnktor"
version = "1.0-SNAPSHOT"

repositories {
	jcenter()
	mavenCentral()
	maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
	implementation("io.ktor:ktor-server-core:$ktorVersion")
	implementation("io.ktor:ktor-server-netty:$ktorVersion")

	implementation("ch.qos.logback:logback-classic:$logbackVersion")

	implementation("io.ktor:ktor-serialization:$ktorVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerialization")

	implementation("io.ktor:ktor-gson:$ktorVersion")

	implementation("org.jetbrains.exposed:exposed:$exposedVersion")
	implementation("com.h2database:h2:$h2Version")

	testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.jvmTarget = "1.8"
}

application {
	mainClassName = "com.theonlytails.learnktor.ApplicationKt"
}