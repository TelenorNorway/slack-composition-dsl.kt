package no.telenor.slack.meta

import com.slack.api.model.block.LayoutBlock
import okhttp3.internal.toImmutableList

class Blocks : Into<List<LayoutBlock>> {
	internal val elements = mutableListOf<LayoutBlock>()

	internal fun add(block: LayoutBlock) {
		elements.add(block)
	}

	internal fun add(block: Into<LayoutBlock>) {
		if (block is Validate) block.validate()
		elements.add(block.into())
	}

	operator fun LayoutBlock.unaryPlus() {
		elements.add(this)
	}

	operator fun Into<LayoutBlock>.unaryPlus() {
		elements.add(into())
	}

	override fun into() = elements.toImmutableList()

	companion object {
		fun with(block: Blocks.() -> Unit) = Blocks().apply(block).into()
	}
}
