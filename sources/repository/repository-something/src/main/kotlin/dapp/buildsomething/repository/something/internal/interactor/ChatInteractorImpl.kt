package dapp.buildsomething.repository.something.internal.interactor

import dapp.buildsomething.repository.something.interactor.ChatInteractor
import dapp.buildsomething.repository.something.interactor.model.ChatStream
import dapp.buildsomething.repository.something.interactor.model.ChatToolCall
import dapp.buildsomething.repository.something.internal.api.SomethingApi
import dapp.buildsomething.repository.something.internal.api.model.ChatMessage
import dapp.buildsomething.repository.something.internal.api.model.ChatMessagePart
import dapp.buildsomething.repository.something.internal.api.model.ChatRequest
import dapp.buildsomething.repository.something.internal.stream.UiMessageStreamChunk
import dapp.buildsomething.repository.something.internal.stream.UiMessageStreamParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.runningFold
import java.util.UUID

internal class ChatInteractorImpl(
    private val api: SomethingApi,
    private val streamParser: UiMessageStreamParser,
) : ChatInteractor {

    override suspend fun sendMessage(appId: String, text: String): Flow<ChatStream> {
        val request = ChatRequest(
            id = appId,
            message = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = "user",
                parts = listOf(ChatMessagePart(type = "text", text = text)),
            ),
        )
        return streamParser.parse(api.chat(request)).toChatStream()
    }

    override suspend fun getChatHistory(appId: String): List<ChatMessage> {
        return api.getChatHistory(appId)
    }

    override suspend fun resumeStream(appId: String): Flow<ChatStream> {
        return streamParser.parse(api.resumeChatStream(appId)).toChatStream()
    }

    private fun Flow<UiMessageStreamChunk>.toChatStream(): Flow<ChatStream> {
        return runningFold(ChatStream()) { state, chunk ->
            when (chunk) {
                is UiMessageStreamChunk.TextDelta -> state.copy(text = state.text + chunk.delta)

                is UiMessageStreamChunk.ToolInputStart -> {
                    if (state.tools.any { it.id == chunk.toolCallId }) {
                        state
                    } else {
                        state.copy(
                            tools = state.tools + ChatToolCall(
                                id = chunk.toolCallId,
                                name = chunk.toolName,
                            ),
                        )
                    }
                }

                is UiMessageStreamChunk.ToolInputAvailable -> {
                    state.updateTool(chunk.toolCallId) { copy(input = chunk.input) }
                }

                is UiMessageStreamChunk.ToolOutputAvailable -> {
                    state.updateTool(chunk.toolCallId) { copy(output = chunk.output) }
                }

                is UiMessageStreamChunk.ToolOutputError -> {
                    state.updateTool(chunk.toolCallId) { copy(error = chunk.errorText) }
                }

                is UiMessageStreamChunk.Error -> {
                    state.copy(error = chunk.errorText)
                }

                is UiMessageStreamChunk.MessageFinish -> {
                    state.copy(isFinished = true)
                }

                else -> state
            }
        }.drop(1)
    }

    private inline fun ChatStream.updateTool(
        toolCallId: String,
        transform: ChatToolCall.() -> ChatToolCall,
    ): ChatStream = copy(
        tools = tools.map { if (it.id == toolCallId) it.transform() else it },
    )
}