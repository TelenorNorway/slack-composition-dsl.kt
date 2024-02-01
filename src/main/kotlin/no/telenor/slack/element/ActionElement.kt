package no.telenor.slack.element

import com.slack.api.model.block.element.BlockElement
import no.telenor.slack.meta.Into

sealed class ActionElement(val id: String?) : Into<BlockElement> {
	init {
		require(id == null || id.length <= 255) { "id must be 255 characters or less" }
		require(id == null || id.isNotEmpty()) { "id must be at least 1 character" }
	}
}


