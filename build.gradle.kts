plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.tnn)
	`maven-publish`
}

group = "no.telenor.next"

repositories {
	mavenCentral()
	telenor.public()
}

kotlin.jvmToolchain(21)

dependencies.compileOnly(libs.slack.bolt)

publishing {
	repositories.github.actions()
	publications.create<MavenPublication>("maven") {
		from(components["kotlin"])
	}
}
