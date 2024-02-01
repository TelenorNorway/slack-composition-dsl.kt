package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.PlainTextObject

class Header(val text: String, blockId: String?) : Block(blockId) {
	init {
		require(text.length in 1..150) { "Header text must be between 1 and 150 characters" }
	}

	override fun into(): LayoutBlock = Blocks.header { header ->
		blockId?.let { header.blockId(it) }
		header.text(PlainTextObject(text, true))
	}
}
