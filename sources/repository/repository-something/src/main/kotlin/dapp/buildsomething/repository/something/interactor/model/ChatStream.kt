package dapp.buildsomething.repository.something.interactor.model

data class ChatStream(
    val text: String = "",
    val tools: List<ChatToolCall> = emptyList(),
    val error: String? = null,
    val isFinished: Boolean = false,
)

data class ChatToolCall(
    val id: String,
    val name: String,
    val input: ToolInput? = null,
    val output: ToolOutput? = null,
    val error: String? = null,
)

sealed interface ToolInput {
    data class CreateFile(
        val filePath: String? = null,
        val content: String,
    ) : ToolInput

    data class ReadFile(val filePath: String) : ToolInput
    data class CreateSchema(val sql: String) : ToolInput
    data object ListFiles : ToolInput
    data object DeployPreview : ToolInput
    data class Unknown(val raw: String) : ToolInput
}

sealed interface ToolOutput {
    data class FileCreated(val fileName: String) : ToolOutput
    data class FileList(val files: List<String>) : ToolOutput
    data class DeployResult(val url: String?, val deployedFiles: List<String> = emptyList()) : ToolOutput
    data class Unknown(val raw: String) : ToolOutput
}
