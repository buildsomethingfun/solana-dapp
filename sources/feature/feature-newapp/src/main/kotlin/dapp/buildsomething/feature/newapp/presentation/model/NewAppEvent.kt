package dapp.buildsomething.feature.newapp.presentation.model

import dapp.buildsomething.repository.something.interactor.model.ChatStream
import dapp.buildsomething.repository.something.internal.api.model.ChatMessage

internal sealed interface NewAppEvent {
    data class StreamUpdate(val stream: ChatStream) : NewAppEvent
    data class StreamError(val message: String) : NewAppEvent
    data object StreamCompleted : NewAppEvent
    data class ChatHistoryLoaded(val history: List<ChatMessage>) : NewAppEvent
    data class ChatHistoryError(val message: String) : NewAppEvent
}

internal sealed interface NewAppUIEvent : NewAppEvent {
    data class InputChanged(val text: String) : NewAppUIEvent
    data object SendMessage : NewAppUIEvent
    data class LoadChatHistory(val appId: String, val appName: String) : NewAppUIEvent
    data object PublishClicked : NewAppUIEvent
    data object NewChat : NewAppUIEvent
}
