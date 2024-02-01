package no.telenor.slack.composition

import com.slack.api.model.block.composition.BlockCompositions
import com.slack.api.model.block.composition.TextObject

class Markdown(
	val text: String,
	val verbatim: Boolean = false,
) : Text() {
	override fun into(): TextObject = BlockCompositions.markdownText(text).also { it.verbatim = verbatim }
	override val length: Int = text.length
}
