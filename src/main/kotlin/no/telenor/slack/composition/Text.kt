package no.telenor.slack.composition

import com.slack.api.model.block.composition.TextObject
import no.telenor.slack.block.Context

sealed class Text : Composition<TextObject>(), Context.CompatibleText {
	abstract val length: Int
}
