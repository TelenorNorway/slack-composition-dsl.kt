package no.telenor.slack.element

import com.slack.api.model.block.composition.DispatchActionConfig
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Input

class EmailInput(
	val value: String? = null,
	val placeholder: String? = null,
	val focusOnLoad: Boolean = false,
	id: String? = null,
	val onCharacterEntered: Boolean = false,
	val onEnterPressed: Boolean = false,
) : ActionElement(id), Input.Compatible {
	init {
		require(placeholder == null || placeholder.length in 1..150) {
			"EmailInput placeholder must be between 1 and 150 characters"
		}
	}

	override fun into() = BlockElements.emailTextInput { input ->
		id?.let { input.actionId(it) }
		value?.let { input.initialValue(it) }
		placeholder?.let { input.placeholder(PlainTextObject(it, true)) }
		input.focusOnLoad(focusOnLoad)
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
