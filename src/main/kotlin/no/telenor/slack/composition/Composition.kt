package no.telenor.slack.composition

import com.slack.api.model.block.ContextBlockElement
import no.telenor.slack.meta.Into

sealed class Composition<T : ContextBlockElement> : Into<T>
