package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.TextObject
import com.slack.api.model.block.element.ImageElement
import no.telenor.slack.meta.Into
import no.telenor.slack.meta.Validate

class Context(blockId: String?) : Block(blockId), Validate {
	sealed interface Compatible
	interface CompatibleText : Compatible, Into<TextObject>
	interface CompatibleImage : Compatible, Into<ImageElement>

	private val elements = mutableListOf<Compatible>()

	operator fun Compatible.unaryPlus() {
		if (elements.size == 10) throw IllegalStateException("Context can have at most 10 elements")
		elements.add(this)
	}

	override fun validate() {
		if (elements.size < 1) throw IllegalStateException("Context must have at least 1 element")
	}

	override fun into(): LayoutBlock = Blocks.context { context ->
		blockId?.let { context.blockId(it) }
		context.elements(elements.map {
			when (it) {
				is CompatibleText -> it.into()
				is CompatibleImage -> it.into()
			}
		})
	}

}
