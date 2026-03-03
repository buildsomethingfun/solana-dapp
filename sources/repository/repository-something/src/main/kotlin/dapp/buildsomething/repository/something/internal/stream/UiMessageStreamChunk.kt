package dapp.buildsomething.repository.something.internal.stream

import dapp.buildsomething.repository.something.interactor.model.ToolInput
import dapp.buildsomething.repository.something.interactor.model.ToolOutput

internal sealed interface UiMessageStreamChunk {

    data class MessageStart(
        val messageId: String?,
    ) : UiMessageStreamChunk

    data class MessageFinish(
        val finishReason: String?,
    ) : UiMessageStreamChunk

    data class TextStart(
        val id: String,
    ) : UiMessageStreamChunk

    data class TextDelta(
        val id: String,
        val delta: String,
    ) : UiMessageStreamChunk

    data class TextEnd(
        val id: String,
    ) : UiMessageStreamChunk

    data class ReasoningStart(
        val id: String,
    ) : UiMessageStreamChunk

    data class ReasoningDelta(
        val id: String,
        val delta: String,
    ) : UiMessageStreamChunk

    data class ReasoningEnd(
        val id: String,
    ) : UiMessageStreamChunk

    data class ToolInputStart(
        val toolCallId: String,
        val toolName: String,
    ) : UiMessageStreamChunk

    data class ToolInputDelta(
        val toolCallId: String,
        val inputTextDelta: String,
    ) : UiMessageStreamChunk

    data class ToolInputAvailable(
        val toolCallId: String,
        val toolName: String,
        val input: ToolInput,
    ) : UiMessageStreamChunk

    data class ToolOutputAvailable(
        val toolCallId: String,
        val output: ToolOutput,
    ) : UiMessageStreamChunk

    data class ToolOutputError(
        val toolCallId: String,
        val errorText: String,
    ) : UiMessageStreamChunk

    data object StepStart : UiMessageStreamChunk

    data object StepFinish : UiMessageStreamChunk

    data class Error(
        val errorText: String,
    ) : UiMessageStreamChunk
}
