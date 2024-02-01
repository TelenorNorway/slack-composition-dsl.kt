package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElement
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class Button(
	id: String,
	val text: String,
	val url: String? = null,
	val value: String? = null,
	val style: Style = Style.Default,
	val accessibilityText: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible {

	init {
		require(text.length in 1..75) { "Button text must be between 1 and 75 characters" }
		require(url == null || url.length in 1..3000) { "Button url must be between 1 and 3000 characters" }
		require(value == null || value.length in 1..2000) { "Button value must be between 1 and 2000 characters" }
		require(accessibilityText == null || accessibilityText.length in 1..75) { "Button accessibilityText must be between 1 and 75 characters" }
	}

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

	override fun into(): BlockElement = BlockElements.button { button ->
		button.actionId(id).text(PlainTextObject(text, true))
		confirm?.let { button.confirm(it.into()) }
		url?.let { button.url(it) }
		value?.let { button.value(it) }
		style.value()?.let { button.style(it) }
		accessibilityText?.let { button.accessibilityLabel(it) }
		button
	}

	enum class Style {
		Default,
		Primary,
		Danger;

		fun value(): String? = when (this) {
			Default -> null
			else -> name.lowercase()
		}
	}
}
