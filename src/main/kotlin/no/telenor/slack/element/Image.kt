package no.telenor.slack.element

import com.slack.api.model.block.composition.SlackFileObject
import com.slack.api.model.block.element.BlockElements
import com.slack.api.model.block.element.ImageElement
import no.telenor.slack.block.Context
import no.telenor.slack.block.Section

class Image(
	val alt: String,
	val url: String? = null,
	val fileUrl: String? = null,
	val fileId: String? = null,
) : Section.CompatibleImage, Context.CompatibleImage {
	init {
		require(!(url == null && fileUrl == null && fileId == null)) { "Image must have either url, file url or file id" }
		require(listOfNotNull(url, fileUrl, fileId).size == 1) { "Image can only have one of url, file url or file id" }
		require(alt.length in 1..100) { "Image altText must be between 1 and 2000 characters" }
		require(url == null || url.length in 1..3000) { "Image url must be between 1 and 3000 characters" }
	}

	override fun into(): ImageElement = BlockElements.image { image ->
		image.altText(alt)
		url?.let { image.imageUrl(it) }
		fileUrl?.let { image.slackFile(SlackFileObject.builder().url(it).build()) }
		fileId?.let { image.slackFile(SlackFileObject.builder().id(it).build()) }
		image
	}
}
