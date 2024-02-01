package no.telenor.slack.element

import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Input

class FileInput(
	val maxFiles: Int = 10,
	vararg val fileTypes: String,
	id: String? = null,
) : ActionElement(id), Input.Compatible {
	init {
		require(maxFiles in 1..10) { "FileInput maxFiles must be between 1 and 10" }
	}

	override fun into() = BlockElements.fileInput { input ->
		id?.let { input.actionId(it) }
		if (fileTypes.isNotEmpty()) input.filetypes(fileTypes.toList())
		input.maxFiles(maxFiles)
	}!!
}
