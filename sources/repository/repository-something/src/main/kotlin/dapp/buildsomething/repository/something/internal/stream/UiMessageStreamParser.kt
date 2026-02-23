package dapp.buildsomething.repository.something.internal.stream

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody

internal class UiMessageStreamParser(
    private val json: Json,
) {

    fun parse(responseBody: ResponseBody): Flow<UiMessageStreamChunk> = flow {
        responseBody.use { body ->
            body.byteStream().bufferedReader().use { reader ->
                while (true) {
                    val line = reader.readLine() ?: break
                    if (line.isBlank() || !line.startsWith(DataPrefix)) continue

                    val data = line.removePrefix(DataPrefix).trim()
                    if (data == DoneSignal) break

                    val chunk = parseChunk(data)
                    if (chunk != null) emit(chunk)
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun parseChunk(data: String): UiMessageStreamChunk? {
        val obj = runCatching { json.parseToJsonElement(data).jsonObject }.getOrNull() ?: return null
        val type = obj["type"]?.jsonPrimitive?.contentOrNull ?: return null

        return when (type) {
            "start" -> UiMessageStreamChunk.MessageStart(
                messageId = obj["messageId"]?.jsonPrimitive?.contentOrNull,
            )
            "finish" -> UiMessageStreamChunk.MessageFinish(
                finishReason = obj["finishReason"]?.jsonPrimitive?.contentOrNull,
            )
            "text-start" -> UiMessageStreamChunk.TextStart(
                id = obj.string("id") ?: return null,
            )
            "text-delta" -> UiMessageStreamChunk.TextDelta(
                id = obj.string("id") ?: return null,
                delta = obj.string("delta") ?: return null,
            )
            "text-end" -> UiMessageStreamChunk.TextEnd(
                id = obj.string("id") ?: return null,
            )
            "reasoning-start" -> UiMessageStreamChunk.ReasoningStart(
                id = obj.string("id") ?: return null,
            )
            "reasoning-delta" -> UiMessageStreamChunk.ReasoningDelta(
                id = obj.string("id") ?: return null,
                delta = obj.string("delta") ?: return null,
            )
            "reasoning-end" -> UiMessageStreamChunk.ReasoningEnd(
                id = obj.string("id") ?: return null,
            )
            "tool-input-start" -> UiMessageStreamChunk.ToolInputStart(
                toolCallId = obj.string("toolCallId") ?: return null,
                toolName = obj.string("toolName") ?: return null,
            )
            "tool-input-delta" -> UiMessageStreamChunk.ToolInputDelta(
                toolCallId = obj.string("toolCallId") ?: return null,
                inputTextDelta = obj.string("inputTextDelta") ?: return null,
            )
            "tool-input-available" -> UiMessageStreamChunk.ToolInputAvailable(
                toolCallId = obj.string("toolCallId") ?: return null,
                toolName = obj.string("toolName") ?: return null,
                input = obj["input"] ?: return null,
            )
            "tool-output-available" -> UiMessageStreamChunk.ToolOutputAvailable(
                toolCallId = obj.string("toolCallId") ?: return null,
                output = obj["output"] ?: return null,
            )
            "tool-output-error" -> UiMessageStreamChunk.ToolOutputError(
                toolCallId = obj.string("toolCallId") ?: return null,
                errorText = obj.string("errorText") ?: return null,
            )
            "start-step" -> UiMessageStreamChunk.StepStart
            "finish-step" -> UiMessageStreamChunk.StepFinish
            "error" -> UiMessageStreamChunk.Error(
                errorText = obj.string("errorText") ?: return null,
            )
            else -> null
        }
    }

    private companion object {
        const val DataPrefix = "data: "
        const val DoneSignal = "[DONE]"
    }
}

private fun JsonObject.string(key: String): String? = get(key)?.jsonPrimitive?.content
