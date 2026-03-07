package dapp.buildsomething.feature.newapp.presentation

import dapp.buildsomething.common.arch.tea.component.UiStateMapper
import dapp.buildsomething.feature.newapp.presentation.model.MessageState
import dapp.buildsomething.feature.newapp.presentation.model.NewAppState
import dapp.buildsomething.feature.newapp.ui.model.ChatMessageUi
import dapp.buildsomething.feature.newapp.ui.model.NewAppUiState
import dapp.buildsomething.feature.newapp.ui.model.ToolCallUi
import dapp.buildsomething.repository.something.interactor.model.ChatToolCall
import dapp.buildsomething.repository.something.interactor.model.ToolInput
import dapp.buildsomething.repository.something.interactor.model.ToolOutput

internal class NewAppUiStateMapper : UiStateMapper<NewAppState, NewAppUiState> {

    override fun map(state: NewAppState): NewAppUiState {
        return NewAppUiState(
            title = state.title,
            messages = state.messages.map { it.toUi(state.appId) },
            inputText = state.inputText,
            isStreaming = state.isStreaming,
            isLoadingHistory = state.isLoadingHistory,
            isFullscreen = state.isFullscreen,
        )
    }

    private fun MessageState.toUi(appId: String): ChatMessageUi {
        return ChatMessageUi(
            isUser = isUser,
            text = text,
            tools = tools.map { it.toUi(appId) },
        )
    }

    private fun ChatToolCall.toUi(appId: String): ToolCallUi {
        val done = output != null || error != null
        val err = error != null
        return when (name) {
            "createFile" -> {
                val createInput = input as? ToolInput.CreateFile
                val path = createInput?.filePath ?: "file"
                val ext = path.substringAfterLast('.', "")
                val preview = createInput?.content?.takeIf { done }
                    ?.lines()?.take(5)?.joinToString("\n")
                ToolCallUi.CreateFile(
                    filePath = path,
                    extension = ext,
                    contentPreview = preview,
                    isCompleted = done,
                    hasError = err,
                )
            }
            "readFile" -> {
                val path = (input as? ToolInput.ReadFile)?.filePath ?: "file"
                val ext = path.substringAfterLast('.', "")
                ToolCallUi.ReadFile(
                    filePath = path,
                    extension = ext,
                    isCompleted = done,
                    hasError = err,
                )
            }
            "createSchema" -> {
                val sql = (input as? ToolInput.CreateSchema)?.sql
                    ?.takeIf { done }?.lines()?.take(5)?.joinToString("\n")
                ToolCallUi.CreateSchema(
                    sqlPreview = sql,
                    isCompleted = done,
                    hasError = err,
                )
            }
            "listFiles" -> {
                val files = (output as? ToolOutput.FileList)?.files.orEmpty()
                ToolCallUi.ListFiles(
                    files = files,
                    isCompleted = done,
                    hasError = err,
                )
            }
            "deployPreview" -> {
                val url = (output as? ToolOutput.DeployResult)?.url
                ToolCallUi.DeployPreview(
                    url = url,
                    appId = appId,
                    isCompleted = done,
                    hasError = err,
                )
            }
            else -> ToolCallUi.Generic(
                label = name,
                isCompleted = done,
                hasError = err,
            )
        }
    }
}
