package no.telenor.slack.element

import com.slack.api.model.block.composition.OptionObject
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation
import no.telenor.slack.meta.Validate

class OverflowMenu(
	id: String? = null,
) : ActionElement(id), Validate, Section.CompatibleElement, Actions.Compatible {
	private var confirm: Confirmation? = null

	fun confirmation(
		title: String,
		text: String,
		confirm: String,
		deny: String,
		style: Confirmation.Style = Confirmation.Style.Default,
	) {
		require(this.confirm == null) { "Button can only have one confirmation" }
		this.confirm = Confirmation(title, text, confirm, deny, style)
	}

	private val options = LinkedHashMap<String, Option>()

	fun option(value: String, text: String, description: String? = null, url: String? = null) {
		require(value.length in 1..75) { "Button text must be between 1 and 75 characters" }
		require(text.length in 1..75) { "Button text must be between 1 and 75 characters" }
		require(url == null || url.length in 1..3000) { "Button url must be between 1 and 3000 characters" }
		require(description == null || description.length in 1..75) { "Button accessibilityText must be between 1 and 75 characters" }
		require(!options.containsKey(value)) { "Option with value $value already exists" }
		require(options.size != 5) { "Overflow menu can only have 5 options" }
		options[value] = Option(value, text, description, url)
	}

	override fun validate() {
		require(options.isNotEmpty()) { "Overflow menu must have at least one option" }
		require(options.size <= 5) { "Overflow menu can only have 5 options" }
	}

	override fun into() = BlockElements.overflowMenu { menu ->
		menu.actionId(id)
		confirm?.let { menu.confirm(it.into()) }
		menu.options(options.map { (_, option) ->
			OptionObject.builder()
				.value(option.value)
				.text(PlainTextObject(option.text, true))
				.also { option.description?.let { desc -> it.description(PlainTextObject(desc, true)) } }
				.also { option.url?.let { url -> it.url(url) } }
				.build()
		})
		menu
	}!!

	data class Option(val value: String, val text: String, val description: String?, val url: String?)
}
