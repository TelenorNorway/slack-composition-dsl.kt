package no.telenor.slack.composition

import com.slack.api.model.block.composition.ConfirmationDialogObject
import com.slack.api.model.block.composition.PlainTextObject
import no.telenor.slack.meta.Into

data class Confirmation(
	val title: String,
	val text: String,
	val confirm: String,
	val deny: String,
	val style: Style = Style.Default,
) : Into<ConfirmationDialogObject> {

	init {
		require(title.length in 1..100) { "Confirmation title must be between 1 and 100 characters" }
		require(text.length in 1..300) { "Confirmation text must be between 1 and 300 characters" }
		require(confirm.length in 1..30) { "Confirmation confirm button text must be between 1 and 30 characters" }
		require(deny.length in 1..30) { "Confirmation deny button text must be between 1 and 30 characters" }
	}

	override fun into() = ConfirmationDialogObject.builder()
		.title(PlainTextObject(title, true))
		.text(PlainTextObject(text, true))
		.confirm(PlainTextObject(confirm, true))
		.deny(PlainTextObject(deny, true))
		.style(style.value())
		.build()!!

	enum class Style {
		Default,
		Primary,
		Danger;

		fun value(): String? = when (this) {
			Default -> null
			else -> this.name.lowercase()
		}
	}
}
