package no.telenor.slack.block

import com.slack.api.model.block.LayoutBlock
import no.telenor.slack.meta.Into

sealed class Block(val blockId: String? = null) : Into<LayoutBlock> {
	init {
		blockId?.let {
			if (it.length > 255) throw IllegalStateException("Section blockId must be less than 255 characters")
			it.ifEmpty { throw IllegalStateException("Section blockId must be at least 1 character") }
		}
	}
}
