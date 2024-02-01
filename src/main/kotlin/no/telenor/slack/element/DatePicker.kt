package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.composition.Confirmation

private val dateRegex = Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}")

class DatePicker(
	/** Format is `YYYY-MM-DD`. */
	val initialDate: String? = null,
	val placeholder: String? = null,
	val focusOnLoad: Boolean = false,
	id: String?,
) : ActionElement(id), Actions.Compatible, Input.Compatible {

	init {
		require(initialDate == null || dateRegex.matches(initialDate)) {
			"DatePicker initialDate must be in format YYYY-MM-DD"
		}
		require(placeholder == null || placeholder.length in 1..150) {
			"DatePicker placeholder must be between 1 and 150 characters"
		}
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

	override fun into() = com.slack.api.model.block.element.BlockElements.datePicker { datePicker ->
		id?.let { datePicker.actionId(it) }
		initialDate?.let { datePicker.initialDate(it) }
		placeholder?.let { datePicker.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { datePicker.confirm(it) }
		datePicker
	}!!
}
