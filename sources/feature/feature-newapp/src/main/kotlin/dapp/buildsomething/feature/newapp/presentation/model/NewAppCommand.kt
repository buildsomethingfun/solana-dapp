package dapp.buildsomething.feature.newapp.presentation.model

internal sealed interface NewAppCommand {
    data class SendMessage(val appId: String, val text: String) : NewAppCommand
    data class LoadChatHistory(val appId: String) : NewAppCommand
}
