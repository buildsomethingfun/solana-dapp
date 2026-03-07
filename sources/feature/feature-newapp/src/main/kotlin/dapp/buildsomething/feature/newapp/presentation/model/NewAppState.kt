package dapp.buildsomething.feature.newapp.presentation.model

import dapp.buildsomething.repository.something.interactor.model.ChatToolCall
import java.util.UUID

internal data class NewAppState(
    val appId: String = UUID.randomUUID().toString(),
    val title: String = "New App",
    val messages: List<MessageState> = emptyList(),
    val inputText: String = "",
    val isStreaming: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val isFullscreen: Boolean = false,
)

internal data class MessageState(
    val isUser: Boolean,
    val text: String,
    val tools: List<ChatToolCall> = emptyList(),
)
