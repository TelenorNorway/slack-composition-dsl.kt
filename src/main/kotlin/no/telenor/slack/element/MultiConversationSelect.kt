package no.telenor.slack.element

import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElements
import com.slack.api.model.block.element.ConversationsFilter
import no.telenor.slack.block.Actions
import no.telenor.slack.block.Input
import no.telenor.slack.block.Section
import no.telenor.slack.composition.Confirmation

class MultiConversationSelect(
	private val initialConversationIds: List<String>? = null,
	private val defaultToCurrentConversation: Boolean = false,
	private val placeholder: String? = null,
	private val focusOnLoad: Boolean = false,
	private val excludeBotUsers: Boolean = false,
	private val excludeExternalSharedChannels: Boolean = false,
	private val includeIM: Boolean = false,
	private val includeMPIM: Boolean = false,
	private val includePrivate: Boolean = false,
	private val includePublic: Boolean = false,
	private val max: Int? = null,
	id: String? = null,
) : ActionElement(id), Section.CompatibleElement, Actions.Compatible, Input.Compatible {
	init {
		require(placeholder == null || placeholder.length <= 150) { "Placeholder text cannot be longer than 150 characters" }
		require(max == null || max in 1..100) { "Max must be between 1 and 100" }
		require(initialConversationIds == null || initialConversationIds.isNotEmpty()) { "Initial conversation IDs must be non-empty" }
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

	override fun into() = BlockElements.multiConversationsSelect { select ->
		id?.let { select.actionId(it) }
		placeholder?.let { select.placeholder(PlainTextObject(it, true)) }
		confirm?.into()?.let { select.confirm(it) }
		initialConversationIds?.let { select.initialConversations(it.toList()) }
		max?.let { select.maxSelectedItems(it) }
		select.defaultToCurrentConversation(defaultToCurrentConversation).focusOnLoad(focusOnLoad)

		val includeList = mutableListOf(
			if (includeIM) "im" else null,
			if (includeMPIM) "mpim" else null,
			if (includePrivate) "private" else null,
			if (includePublic) "public" else null,
		).filterNotNull()

		if (includeList.isNotEmpty() || excludeBotUsers || excludeExternalSharedChannels) {
			val builder = ConversationsFilter.builder()
			if (includeList.isNotEmpty()) builder.include(includeList)
			if (excludeBotUsers) builder.excludeBotUsers(true)
			if (excludeExternalSharedChannels) builder.excludeExternalSharedChannels(true)
			select.filter(builder.build())
		}

		select
	}!!
}
