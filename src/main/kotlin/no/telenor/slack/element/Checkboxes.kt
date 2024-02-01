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

class Checkboxes(
	val focusOnLoad: Boolean = false,
	id: String? = null
) : ActionElement(id), Actions.Compatible, Section.CompatibleElement, Input.Compatible {
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
	private val selectedOptions = mutableListOf<String>()

	fun option(value: String, description: String? = null, selected: Boolean = false, block: () -> Text) {
		require(options.size != 10) { "Checkbox can have at most 10 options" }
		require(value.length in 1..75) { "Checkbox option value must be between 1 and 75 characters" }
		require(description == null || description.length in 1..75) { "Checkbox option description must be between 1 and 75 characters" }
		require(!options.containsKey(value)) { "Checkbox option value must be unique" }
		options[value] = Option(block(), description)
		if (selected) selectedOptions.add(value)
	}

	private data class Option(val text: Text, val description: String?)

	override fun into(): BlockElement = BlockElements.checkboxes { checkboxes ->
		id?.let { checkboxes.actionId(it) }
		confirm?.into()?.let { checkboxes.confirm(it) }
		val opts = options.map { (key, value) ->
			val newValue = OptionObject.builder().value(key).text(value.text.into()).also {
				value.description?.let { desc -> it.description(PlainTextObject(desc, true)) }
			}.build()
			key to newValue
		}.toMap()
		checkboxes
			.options(opts.values.toList())
			.initialOptions(opts.filterKeys { selectedOptions.contains(it) }.values.toList())
	}
}
