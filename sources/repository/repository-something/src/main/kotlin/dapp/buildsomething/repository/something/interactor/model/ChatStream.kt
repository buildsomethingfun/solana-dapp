package dapp.buildsomething.repository.something.interactor.model

import kotlinx.serialization.json.JsonElement

data class ChatStream(
    val text: String = "",
    val tools: List<ChatToolCall> = emptyList(),
    val error: String? = null,
    val isFinished: Boolean = false,
)

data class ChatToolCall(
    val id: String,
    val name: String,
    val input: JsonElement? = null,
    val output: JsonElement? = null,
    val error: String? = null,
)
