package dapp.buildsomething.feature.newapp.ui.model

internal data class NewAppUiState(
    val title: String = "New App",
    val messages: List<ChatMessageUi> = emptyList(),
    val inputText: String = "",
    val isStreaming: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val isFullscreen: Boolean = false,
)

internal data class ChatMessageUi(
    val isUser: Boolean,
    val text: String,
    val tools: List<ToolCallUi> = emptyList(),
)

internal sealed interface ToolCallUi {
    val isCompleted: Boolean
    val hasError: Boolean

    data class CreateFile(
        val filePath: String,
        val extension: String,
        val contentPreview: String? = null,
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi

    data class ReadFile(
        val filePath: String,
        val extension: String,
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi

    data class CreateSchema(
        val sqlPreview: String? = null,
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi

    data class ListFiles(
        val files: List<String> = emptyList(),
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi

    data class DeployPreview(
        val url: String? = null,
        val appId: String? = null,
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi

    data class Generic(
        val label: String,
        override val isCompleted: Boolean = false,
        override val hasError: Boolean = false,
    ) : ToolCallUi
}
