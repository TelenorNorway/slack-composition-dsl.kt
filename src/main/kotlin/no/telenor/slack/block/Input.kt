package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElement
import no.telenor.slack.meta.Into

class Input(
	val label: String,
	val element: Compatible,
	val dispatchAction: Boolean = false,
	val hint: String? = null,
	val optional: Boolean = false,
	blockId: String? = null,
) : Block(blockId) {
	interface Compatible : Into<BlockElement>

	init {
		require(label.length in 1..2000) { "Input label must be between 1 and 2000 characters" }
		require(hint == null || hint.length in 1..2000) { "Input hint must be between 1 and 2000 characters" }
	}

	override fun into(): LayoutBlock = Blocks.input { input ->
		blockId?.let { input.blockId(it) }
		hint?.let { input.hint(PlainTextObject(it, true)) }
		input
			.label(PlainTextObject(label, true))
			.element(element.into())
			.dispatchAction(dispatchAction)
			.optional(optional)
	}
}
