package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock

class File(val id: String, blockId: String?) : Block(blockId) {
	override fun into(): LayoutBlock = Blocks.file { file ->
		blockId?.let { file.blockId(it) }
		file.source("remote").externalId(id)
	}
}
