package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val id: String,
    val message: ChatMessage,
)

@Serializable
data class ChatMessage(
    val id: String,
    val role: String,
    val parts: List<ChatMessagePart>,
)

@Serializable
data class ChatMessagePart(
    val type: String,
    val text: String? = null,
    val toolName: String? = null,
    val state: String? = null,
    val args: Map<String, String>? = null,
    val result: String? = null,
)
