package dapp.buildsomething.feature.newapp.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.newapp.presentation.model.MessageState
import dapp.buildsomething.feature.newapp.presentation.model.NewAppCommand
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEffect
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEvent
import dapp.buildsomething.feature.newapp.presentation.model.NewAppState
import dapp.buildsomething.feature.newapp.presentation.model.NewAppUIEvent
import java.util.UUID

internal class NewAppReducer :
    DslReducer<NewAppCommand, NewAppEffect, NewAppEvent, NewAppState>() {

    override fun reduce(event: NewAppEvent) {
        when (event) {

            is NewAppUIEvent.InputChanged -> {
                state { copy(inputText = event.text) }
            }

            is NewAppUIEvent.SendMessage -> {
                val text = state.inputText.trim()

                if (text.isEmpty()) return

                state {
                    copy(
                        messages = messages + MessageState(isUser = true, text = text),
                        inputText = "",
                        isStreaming = true,
                    )
                }

                commands(NewAppCommand.SendMessage(appId = state.appId, text = text))
            }

            is NewAppEvent.StreamUpdate -> {
                val stream = event.stream

                val assistantMessage = MessageState(
                    isUser = false,
                    text = stream.text,
                    tools = stream.tools,
                )

                state {
                    val updated = if (messages.lastOrNull()?.isUser == false) {
                        messages.dropLast(1) + assistantMessage
                    } else {
                        messages + assistantMessage
                    }
                    copy(messages = updated)
                }
            }

            is NewAppEvent.StreamCompleted -> {
                state { copy(isStreaming = false) }
            }

            is NewAppEvent.StreamError -> {
                state { copy(isStreaming = false) }
                effects(NewAppEffect.ShowError(event.message))
            }

            is NewAppUIEvent.LoadChatHistory -> {
                state {
                    copy(
                        appId = event.appId,
                        title = event.appName,
                        isLoadingHistory = true,
                        isFullscreen = event.appId.isNotEmpty()
                    )
                }
                commands(NewAppCommand.LoadChatHistory(appId = event.appId))
            }

            is NewAppEvent.ChatHistoryLoaded -> {
                val messages = event.history.map { msg ->
                    MessageState(
                        isUser = msg.role == "user",
                        text = msg.parts
                            .filter { it.type == "text" }
                            .mapNotNull { it.text }
                            .joinToString(""),
                    )
                }
                state { copy(messages = messages, isLoadingHistory = false) }
            }

            is NewAppEvent.ChatHistoryError -> {
                state { copy(isLoadingHistory = false) }
                effects(NewAppEffect.ShowError(event.message))
            }

            is NewAppUIEvent.PublishClicked -> {
                effects(NewAppEffect.NavigateToAppDetail(state.appId))
            }

            is NewAppUIEvent.NewChat -> {
                state {
                    NewAppState(appId = UUID.randomUUID().toString())
                }
            }
        }
    }
}
