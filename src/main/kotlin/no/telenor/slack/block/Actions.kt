package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.element.BlockElement
import no.telenor.slack.meta.Into
import no.telenor.slack.meta.Validate

class Actions(blockId: String?) : Block(blockId), Validate {
	interface Compatible : Into<BlockElement>

	internal val elements = mutableListOf<Compatible>()

	operator fun Compatible.unaryPlus() {
		if (this is Validate) validate()
		if (elements.size == 25) throw IllegalStateException("Actions can have at most 25 elements")
		elements.add(this)
	}

	override fun validate() {
		if (elements.size < 1) {
			throw IllegalStateException("Actions must have at least 1 element")
		}
	}

	override fun into(): LayoutBlock = Blocks.actions { actions ->
		blockId?.let { actions.blockId(it) }
		actions.elements(elements.map { it.into() })
	}
}
