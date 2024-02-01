package no.telenor.slack.block

import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.RichTextBlock
import com.slack.api.model.block.element.*
import no.telenor.slack.meta.Dsl
import no.telenor.slack.meta.Into
import no.telenor.slack.meta.Validate

class RichText(blockId: String?) : Block(blockId) {

	private val elements = mutableListOf<BlockElement>()

	sealed interface RootElement : Into<RichTextElement> {
		fun blockElement(): BlockElement {
			return this.into() as BlockElement
		}
	}

	operator fun RootElement.unaryPlus() {
		elements.add(this.blockElement())
	}

	override fun into(): LayoutBlock {
		val builder = RichTextBlock.builder()
		blockId?.let { builder.blockId(it) }
		return builder.elements(elements).build()
	}

	class Section(block: (@Dsl Section).() -> Unit = {}) : RootElement {
		private val elements = mutableListOf<RichTextElement>()

		init {
			apply(block)
		}

		operator fun RichTextElement.unaryPlus() {
			elements.add(this)
		}

		operator fun Into<RichTextElement>.unaryPlus() {
			if (this is Validate) validate()
			elements.add(this.into())
		}

		override fun into() = RichTextSectionElement.builder().elements(elements).build()!!
	}

	class List(
		val style: Style,
		val indent: Int? = null,
		val offset: Int? = null,
		val border: Int? = null,
		block: (@Dsl List).() -> Unit = {}
	) : RootElement {
		private val elements = mutableListOf<Section>()

		init {
			apply(block)
		}

		operator fun Section.unaryPlus() {
			elements.add(this)
		}

		override fun into() = RichTextListElement.builder().elements(elements.map { it.into() }).build()!!

		enum class Style {
			Ordered,
			Bullet;

			fun value() = name.lowercase()
		}
	}

	class Preformatted(val border: Int?, block: (@Dsl Preformatted).() -> Unit) : RootElement {
		private val elements = mutableListOf<RichTextElement>()

		init {
			apply(block)
		}

		operator fun RichTextElement.unaryPlus() {
			elements.add(this)
		}

		operator fun Into<RichTextElement>.unaryPlus() {
			if (this is Validate) validate()
			elements.add(this.into())
		}

		override fun into() = RichTextPreformattedElement.builder().elements(elements).build()!!
	}

	class Quote(block: (@Dsl Quote).() -> Unit) : RootElement {
		private val elements = mutableListOf<RichTextElement>()

		init {
			apply(block)
		}

		operator fun RichTextElement.unaryPlus() {
			elements.add(this)
		}

		operator fun Into<RichTextElement>.unaryPlus() {
			if (this is Validate) validate()
			elements.add(this.into())
		}

		override fun into() = RichTextQuoteElement.builder().elements(elements).build()!!
	}

	open class Text(
		val text: String,
		val bold: Boolean? = null,
		val italic: Boolean? = null,
		val code: Boolean? = null,
		val strike: Boolean? = null
	) : Into<RichTextElement> {
		override fun into() = RichTextSectionElement.Text.builder()
			.text(text.trimIndent())
			.style(
				RichTextSectionElement.TextStyle.builder()
					.also { style -> bold?.let { style.bold(it) } }
					.also { style -> italic?.let { style.italic(it) } }
					.also { style -> code?.let { style.code(it) } }
					.also { style -> strike?.let { style.strike(it) } }
					.build()
			).build()!!
	}

	class Paragraph(
		text: String,
		bold: Boolean? = null,
		italic: Boolean? = null,
		code: Boolean? = null,
		strike: Boolean? = null
	) : Text(text + "\n\n", bold, italic, code, strike)
}
