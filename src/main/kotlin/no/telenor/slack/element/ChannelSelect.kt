package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class ChannelSelect(
	private val initialChannelId: String? = null,
	private val placeholder: String? = null,
	private val focusOnLoad: Boolean = false,
	private val responseUrlEnabled: Boolean? = null,
	id: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible, Input.Compatible {
	init {
		require(placeholder == null || placeholder.length <= 150) { "Placeholder text cannot be longer than 150 characters" }
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

	override fun into() = BlockElements.channelsSelect { select ->
		id?.let { select.actionId(it) }
		placeholder?.let { select.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { select.confirm(it) }
		initialChannelId?.let { select.initialChannel(it) }
		responseUrlEnabled?.let { select.responseUrlEnabled(it) }
		select.focusOnLoad(focusOnLoad)
		select
	}!!
}
