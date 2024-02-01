package no.telenor.slack.composition

import com.slack.api.model.block.composition.BlockCompositions
import com.slack.api.model.block.composition.TextObject

class PlainText(
	val text: String,
	val emoji: Boolean = true,
) : Text() {
	override fun into(): TextObject = BlockCompositions.plainText(text, emoji)
	override val length: Int = text.length
}
