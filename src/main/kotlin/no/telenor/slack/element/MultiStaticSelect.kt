package no.telenor.slack.element

import com.slack.api.model.block.composition.OptionGroupObject
import com.slack.api.model.block.composition.OptionObject
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class MultiStaticSelect(
	private val placeholder: String? = null,
	private val focusOnLoad: Boolean = false,
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

	private val selectedOptions = mutableListOf<String>()

	private val options = LinkedHashMap<String, OptionObject>()
	private val groups = LinkedHashMap<String, MutableList<String>>()
	private var currentGroup: String? = null

	fun option(value: String, text: String, description: String? = null, selected: Boolean = false) {
		if (groups.isNotEmpty() && currentGroup == null) {
			throw IllegalStateException("Static select option must be in a group")
		}
		require(options.size != 100) { "Static select can have at most 100 options" }
		require(value.length in 1..75) { "Static select option value must be between 1 and 75 characters" }
		require(description == null || description.length in 1..75) { "Static select option description must be between 1 and 75 characters" }
		require(!options.containsKey(value)) { "Static select option value must be unique" }
		val optionObject = OptionObject.builder().value(value).text(PlainTextObject(text, true)).also {
			description?.let { desc -> it.description(PlainTextObject(desc, true)) }
		}.build()
		if (selected) selectedOptions.add(value)
		options[value] = optionObject
		if (currentGroup != null) {
			groups[currentGroup]!!.add(value)
		}
	}

	fun group(text: String, block: OptionProxy.() -> Unit) {
		require(text.length in 1..75) { "Static select group text must be between 1 and 75 characters" }
		require(!groups.containsKey(text)) { "Static select group text must be unique" }
		groups[text] = mutableListOf()
		val beforeLength = options.size
		currentGroup = text
		OptionProxy(this).apply(block)
		currentGroup = null
		require(options.size - beforeLength > 0) { "Static select group must have at least one option" }
	}

	class OptionProxy(private val self: MultiStaticSelect) {
		fun option(value: String, text: String, description: String? = null, selected: Boolean = false) =
			self.option(value, text, description, selected)
	}

	override fun into() = BlockElements.multiStaticSelect { select ->
		id?.let { select.actionId(it) }
		placeholder?.let { select.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { select.confirm(it) }
		if (selectedOptions.isNotEmpty()) select.initialOptions(selectedOptions.map { options[it] })
		select.maxSelectedItems(max)
		select.focusOnLoad(focusOnLoad)
		if (groups.isEmpty()) {
			select.options(options.values.toList())
		} else {
			select.optionGroups(groups.map { (label, values) ->
				OptionGroupObject.builder().label(PlainTextObject(label, true)).options(values.map { options[it] }).build()
			})
		}
		select
	}!!
}
