package no.telenor.slack.block

import com.slack.api.model.block.Blocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.element.BlockElement
import com.slack.api.model.block.element.ImageElement
import no.telenor.slack.composition.Text
import no.telenor.slack.meta.Into
import no.telenor.slack.meta.Validate

class Section(blockId: String?) : Block(blockId), Validate {
	sealed interface Compatible
	interface CompatibleElement : Compatible, Into<BlockElement>
	interface CompatibleImage : Compatible, Into<ImageElement>

	internal var textData: Text? = null
	internal var accessoryData: Compatible? = null

	var text: Text?
		get() = textData
		set(value) {
			textData = value!!.let {
				if (it.length > 3000) {
					throw IllegalStateException("Section text must be less than 3000 characters")
				}
				if (it.length < 1) {
					throw IllegalStateException("Section text must be at least 1 character")
				}
				it
			}
		}

	var accessory: Compatible?
		get() = accessoryData
		set(value) {
			accessoryData = value!!.let {
				if (it is Validate) it.validate()
				it
			}
		}

	internal val fields = mutableListOf<Text>()

	operator fun Text.unaryPlus() {
		if (length > 2000) {
			throw IllegalStateException("Section field text must be less than 2000 characters")
		}
		if (length < 1) {
			throw IllegalStateException("Section field text must be at least 1 character")
		}
		if (fields.size == 10) {
			throw IllegalStateException("Section can have at most 10 fields")
		}
		fields.add(this)
	}

	override fun into(): LayoutBlock = Blocks.section { section ->
		blockId?.let { section.blockId(it) }
		textData?.into()?.let { section.text(it) }
		accessoryData?.let {
			when (it) {
				is CompatibleElement -> it.into()
				is CompatibleImage -> it.into()
			}
		}?.let { section.accessory(it) }
		if (fields.size > 0) section.fields(fields.map { it.into() })
		section
	}

	override fun validate() {
		if (textData == null && fields.size == 0) {
			throw IllegalStateException("Section must have either text or fields")
		}
	}
}
