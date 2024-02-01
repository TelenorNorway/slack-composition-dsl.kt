package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class MultiChannelSelect(
	private val initialChannelIds: List<String>? = null,
	private val placeholder: String? = null,
	private val focusOnLoad: Boolean = false,
	private val max: Int? = null,
	id: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible, Input.Compatible {
	init {
		require(placeholder == null || placeholder.length <= 150) { "Placeholder text cannot be longer than 150 characters" }
		require(max == null || max in 1..100) { "Max must be between 1 and 100" }
		require(initialChannelIds == null || initialChannelIds.isNotEmpty()) { "Initial channel IDs must be non-empty" }
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

	override fun into() = BlockElements.multiChannelsSelect { select ->
		id?.let { select.actionId(it) }
		placeholder?.let { select.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { select.confirm(it) }
		initialChannelIds?.let { select.initialChannels(it.toList()) }
		max?.let { select.maxSelectedItems(it) }
		select.focusOnLoad(focusOnLoad)
		select
	}!!
}
