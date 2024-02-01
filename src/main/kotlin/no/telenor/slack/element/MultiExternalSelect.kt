package no.telenor.slack.element

import com.slack.api.model.block.composition.OptionObject
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class MultiExternalSelect(
	private val placeholder: String? = null,
	private val minQueryLength: Int? = null,
	private val focusOnLoad: Boolean? = null,
	private val max: Int? = null,
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

	private var selectedOptions = LinkedHashMap<String, OptionObject>()

	fun initialOption(value: String, text: String, description: String? = null) {
		require(value.length in 1..75) { "Static select option value must be between 1 and 75 characters" }
		require(description == null || description.length in 1..75) { "Static select option description must be between 1 and 75 characters" }
		require(!selectedOptions.containsKey(value)) { "Static select option value must be unique" }
		selectedOptions[value] = OptionObject.builder().value(value).text(PlainTextObject(text, true)).also {
			description?.let { desc -> it.description(PlainTextObject(desc, true)) }
		}.build()
	}

	override fun into() = BlockElements.multiExternalSelect { select ->
		id?.let { select.actionId(it) }
		placeholder?.let { select.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { select.confirm(it) }
		max?.let { select.maxSelectedItems(it) }
		if (selectedOptions.isNotEmpty()) select.initialOptions(selectedOptions.values.toList())
		minQueryLength?.let { select.minQueryLength(it) }
		select.focusOnLoad(focusOnLoad)
	}!!
}
