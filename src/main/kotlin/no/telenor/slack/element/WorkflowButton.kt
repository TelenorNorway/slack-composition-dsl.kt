package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.composition.WorkflowObject
import com.slack.api.model.block.element.BlockElements
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Section

class WorkflowButton(
	id: String,
	val text: String,
	val workflowUrl: String,
	vararg val inputParameters: Pair<String, String>,
	val style: Button.Style,
	val accessibilityLabel: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible {
	init {
		require(text.length in 1..75) { "Button text must be between 1 and 75 characters" }
		require(workflowUrl.length in 1..3000) { "Button workflowUrl must be between 1 and 3000 characters" }
		require(accessibilityLabel == null || accessibilityLabel.length in 1..75) { "Button accessibilityLabel must be between 1 and 75 characters" }
	}

	override fun into() = BlockElements.workflowButton { button ->
		button.actionId(id).text(PlainTextObject(text, true))
			.workflow(
				WorkflowObject.builder()
					.trigger(
						WorkflowObject.Trigger.builder()
							.url(workflowUrl)
							.customizableInputParameters(inputParameters.map {
								WorkflowObject.Trigger.InputParameter.builder().name(it.first).value(it.second).build()
							}).build()
					).build()
			)
		accessibilityLabel?.let { button.accessibilityLabel(it) }
		button
	}!!
}
