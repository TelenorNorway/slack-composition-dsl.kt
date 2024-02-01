package no.telenor.slack.element

import com.slack.api.model.block.composition.OptionObject
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElement
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation
import no.telenor.slack.composition.Text

class RadioButtonGroup(
	val focusOnLoad: Boolean = false,
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

	private val options = LinkedHashMap<String, OptionObject>()
	private var selectedOption: OptionObject? = null

	fun option(value: String, description: String? = null, selected: Boolean = false, block: () -> Text) {
		require(options.size != 10) { "Radio groups can have at most 10 options" }
		require(value.length in 1..75) { "Radio groups option value must be between 1 and 75 characters" }
		require(description == null || description.length in 1..75) { "Radio groups option description must be between 1 and 75 characters" }
		require(!options.containsKey(value)) { "Radio groups option value must be unique" }
		val optionObject = OptionObject.builder().value(value).text(block().into()).also {
			description?.let { desc -> it.description(PlainTextObject(desc, true)) }
		}.build()
		options[value] = optionObject
		if (selected) selectedOption = optionObject
	}

	override fun into(): BlockElement = BlockElements.radioButtons { grp ->
		id?.let { grp.actionId(it) }
		confirm?.into()?.let { grp.confirm(it) }
		selectedOption?.let { grp.initialOption(selectedOption) }
		grp.options(options.values.toList())
	}
}
