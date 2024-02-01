package no.telenor.slack.meta

interface Validate {
	@Throws(IllegalStateException::class)
	fun validate()
}
