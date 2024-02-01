rootProject.name = "slack-composition-dsl"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			version("slack", "1.38.0")
			version("kotlin", "1.9.22")

			plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
			plugin("tnn", "sh.tnn").version("0.2.0")
			library("slack-bolt", "com.slack.api", "bolt").versionRef("slack")
		}
	}
}
