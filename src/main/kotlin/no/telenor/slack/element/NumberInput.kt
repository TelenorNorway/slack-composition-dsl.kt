package no.telenor.slack.element

import com.slack.api.model.block.composition.DispatchActionConfig
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Input

class NumberInput(
	val isDecimal: Boolean = false,
	val value: String? = null,
	val min: String? = null,
	val max: String? = null,
	val placeholder: String? = null,
	val focusOnLoad: Boolean = false,
	val onCharacterEntered: Boolean = false,
	val onEnterPressed: Boolean = false,
	id: String? = null
) : ActionElement(id), Input.Compatible {
	init {
		require(placeholder == null || placeholder.length in 1..150) {
			"EmailInput placeholder must be between 1 and 150 characters"
		}
	}

	override fun into() = BlockElements.numberInput { input ->
		id?.let { input.actionId(it) }
		input.initialValue(value)
		input.placeholder(PlainTextObject(placeholder, true))
		input.focusOnLoad(focusOnLoad)
		input.minValue(min)
		input.maxValue(max)
		if (onCharacterEntered || onEnterPressed) {
			input.dispatchActionConfig(
				DispatchActionConfig.builder().triggerActionsOn(
					listOfNotNull(
						if (onCharacterEntered) "on_character_entered" else null,
						if (onEnterPressed) "on_enter_pressed" else null,
					)
				).build()
			)
		}
		input
	}!!
}
