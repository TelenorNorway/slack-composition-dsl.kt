package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.composition.SlackFileObject

class ImageBlock(
	val alt: String,
	val url: String? = null,
	val fileUrl: String? = null,
	val fileId: String? = null,
	val title: String? = null,
	blockId: String? = null,
) : Block(blockId) {
	init {
		require(!(url == null && fileUrl == null && fileId == null)) { "Image must have either url, file url or file id" }
		require(listOfNotNull(url, fileUrl, fileId).size == 1) { "Image can only have one of url, file url or file id" }
		require(alt.length in 1..100) { "Image altText must be between 1 and 2000 characters" }
		require(url == null || url.length in 1..3000) { "Image url must be between 1 and 3000 characters" }
		require(title == null || title.length in 1..2000) { "Image text must be between 1 and 2000 characters" }
	}

	override fun into(): LayoutBlock = Blocks.image { image ->
		blockId?.let { image.blockId(it) }
		image.altText(alt)
		title?.let { image.title(PlainTextObject(it, true)) }
		url?.let { image.imageUrl(it) }
		fileUrl?.let { image.slackFile(SlackFileObject.builder().url(it).build()) }
		fileId?.let { image.slackFile(SlackFileObject.builder().id(it).build()) }
		image
	}
}
