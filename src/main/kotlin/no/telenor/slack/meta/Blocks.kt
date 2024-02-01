package no.telenor.slack.meta

import com.slack.api.model.block.LayoutBlock

class Blocks {
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
}
