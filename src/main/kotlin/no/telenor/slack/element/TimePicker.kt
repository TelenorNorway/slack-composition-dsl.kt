package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation
import java.time.ZoneId

class TimePicker(
	/** HH:mm 24-hour format. */
	val value: String? = null,
	val placeholder: String? = null,
	val focusOnLoad: Boolean = false,
	val timezone: ZoneId? = null,
	id: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible, Input.Compatible {
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

	override fun into() = BlockElements.timePicker { input ->
		id?.let { input.actionId(it) }
		value?.let { input.initialTime(it) }
		placeholder?.let { input.placeholder(PlainTextObject(it, true)) }
		input.focusOnLoad(focusOnLoad)
		timezone?.let { input.timezone(it.id) }
		confirm?.let { input.confirm(it.into()) }
		input
	}!!
}
