package no.telenor.slack

import com.slack.api.methods.request.chat.ChatPostMessageRequest
import no.telenor.slack.block.*
import no.telenor.slack.composition.Markdown
import no.telenor.slack.composition.PlainText
import no.telenor.slack.composition.Text
import no.telenor.slack.element.*
import no.telenor.slack.meta.Blocks
import no.telenor.slack.meta.Dsl
import java.time.ZoneId

// Blocks
fun actions(blockId: String? = null, block: (@Dsl Actions).() -> Unit) = Actions(blockId).apply(block)
fun context(blockId: String? = null, block: (@Dsl Context).() -> Unit) = Context(blockId).apply(block)
fun divider(blockId: String? = null) = Divider(blockId)
fun fileBlock(id: String, blockId: String? = null) = File(id, blockId)
fun header(text: String, blockId: String? = null) = Header(text, blockId)
fun imageBlock(
	alt: String,
	url: String? = null,
	fileUrl: String? = null,
	fileId: String? = null,
	title: String? = null,
	blockId: String? = null,
) = ImageBlock(alt, url, fileUrl, fileId, title, blockId)

fun input(
	label: String,
	element: Input.Compatible,
	dispatchAction: Boolean = false,
	hint: String? = null,
	optional: Boolean = false,
	blockId: String? = null,
) = Input(label, element, dispatchAction, hint, optional, blockId)

fun richText(blockId: String? = null, block: (@Dsl RichText).() -> Unit) = RichText(blockId).apply(block)

fun section(blockId: String? = null, block: (@Dsl Section).() -> Unit) = Section(blockId).apply(block)
fun section(text: Text, blockId: String? = null, block: (@Dsl Section).() -> Unit = {}) = Section(blockId)
	.apply { this.textData = text }
	.apply(block)

// Composition

fun markdown(text: String, verbatim: Boolean = false) = Markdown(text.trimIndent(), verbatim)
fun markdown(verbatim: Boolean = false, text: () -> String) = Markdown(text().trimIndent(), verbatim)

fun plain(text: String, emoji: Boolean = true) = PlainText(text.trimIndent(), emoji)
fun plain(emoji: Boolean = true, text: () -> String) = PlainText(text().trimIndent(), emoji)

// Element

fun button(
	id: String,
	text: String,
	url: String? = null,
	value: String? = null,
	style: Button.Style = Button.Style.Default,
	accessibilityText: String? = null,
	block: (@Dsl Button).() -> Unit = {},
) = Button(id, text, url, value, style, accessibilityText).apply(block)

fun checkboxes(
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl Checkboxes).() -> Unit = {},
) = Checkboxes(focusOnLoad, id).apply(block)

fun datePicker(
	initialDate: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl DatePicker).() -> Unit = {},
) = DatePicker(initialDate, placeholder, focusOnLoad, id).apply(block)

fun emailInput(
	value: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	onCharacterEntered: Boolean = false,
	onEnterPressed: Boolean = false,
	id: String? = null,
) = EmailInput(value, placeholder, focusOnLoad, id, onCharacterEntered, onEnterPressed)

fun fileInput(
	maxFiles: Int = 10,
	vararg fileTypes: String,
	id: String? = null,
) = FileInput(maxFiles, *fileTypes, id = id)

fun image(
	alt: String,
	url: String? = null,
	fileUrl: String? = null,
	fileId: String? = null,
) = Image(alt, url, fileUrl, fileId)

fun numberInput(
	isDecimal: Boolean = false,
	value: String? = null,
	min: String? = null,
	max: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	onCharacterEntered: Boolean = false,
	onEnterPressed: Boolean = false,
	id: String? = null
) = NumberInput(isDecimal, value, min, max, placeholder, focusOnLoad, onCharacterEntered, onEnterPressed, id)

fun overflowMenu(
	id: String? = null,
	block: (@Dsl OverflowMenu).() -> Unit,
) = OverflowMenu(id).apply(block)

fun radioGroup(
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl RadioButtonGroup).() -> Unit = {},
) = RadioButtonGroup(focusOnLoad, id).apply(block)

fun textInput(
	value: String? = null,
	multiLine: Boolean = false,
	minLength: Int? = null,
	maxLength: Int? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	onCharacterEntered: Boolean = false,
	onEnterPressed: Boolean = false,
	id: String? = null
) = TextInput(value, multiLine, minLength, maxLength, placeholder, focusOnLoad, onCharacterEntered, onEnterPressed, id)

fun timePicker(
	/** HH:mm 24-hour format. */
	value: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	timezone: ZoneId? = null,
	id: String? = null,
) = TimePicker(value, placeholder, focusOnLoad, timezone, id)

fun urlInput(
	value: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	onCharacterEntered: Boolean = false,
	onEnterPressed: Boolean = false,
	id: String? = null,
) = UrlInput(value, placeholder, focusOnLoad, onCharacterEntered, onEnterPressed, id)

fun workflowButton(
	id: String,
	text: String,
	workflowUrl: String,
	vararg inputParameters: Pair<String, String>,
	style: Button.Style = Button.Style.Default,
	accessibilityLabel: String? = null,
) = WorkflowButton(id, text, workflowUrl, *inputParameters, accessibilityLabel = accessibilityLabel, style = style)

// Select Menu

fun staticSelect(
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl StaticSelect).() -> Unit = {},
) = StaticSelect(placeholder, focusOnLoad, id).apply(block)

fun externalSelect(
	placeholder: String? = null,
	minQueryLength: Int? = null,
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl ExternalSelect).() -> Unit = {},
) = ExternalSelect(placeholder, minQueryLength, focusOnLoad, id).apply(block)

fun userSelect(
	initialUserId: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	id: String? = null,
	block: (@Dsl UserSelect).() -> Unit = {},
) = UserSelect(initialUserId, placeholder, focusOnLoad, id).apply(block)

fun conversationSelect(
	initialConversationId: String? = null,
	defaultToCurrentConversation: Boolean = false,
	placeholder: String? = null,
	responseUrlEnabled: Boolean? = null,
	focusOnLoad: Boolean = false,
	excludeBotUsers: Boolean = false,
	excludeExternalSharedChannels: Boolean = false,
	includeIM: Boolean = false,
	includeMPIM: Boolean = false,
	includePrivate: Boolean = false,
	includePublic: Boolean = false,
	id: String? = null,
) = ConversationSelect(
	initialConversationId,
	defaultToCurrentConversation,
	placeholder,
	responseUrlEnabled,
	focusOnLoad,
	excludeBotUsers,
	excludeExternalSharedChannels,
	includeIM,
	includeMPIM,
	includePrivate,
	includePublic,
	id,
)

fun channelSelect(
	initialChannelId: String? = null,
	placeholder: String? = null,
	focusOnLoad: Boolean = false,
	responseUrlEnabled: Boolean? = null,
	id: String? = null,
	block: (@Dsl ChannelSelect).() -> Unit = {},
) = ChannelSelect(initialChannelId, placeholder, focusOnLoad, responseUrlEnabled, id).apply(block)

// Meta
fun ChatPostMessageRequest.ChatPostMessageRequestBuilder.blocks(block: (@Dsl Blocks).() -> Unit) =
	this.blocks(Blocks().apply(block).elements)!!
