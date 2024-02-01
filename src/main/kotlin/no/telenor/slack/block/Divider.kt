package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock

class Divider(blockId: String?) : Block(blockId) {
	override fun into(): LayoutBlock = blockId?.let { Blocks.divider(it) } ?: Blocks.divider()
}
