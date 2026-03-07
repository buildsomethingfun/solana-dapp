package dapp.buildsomething.feature.newapp.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.newapp.presentation.model.NewAppCommand
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEvent
import dapp.buildsomething.repository.something.interactor.ChatInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class LoadChatHistoryActor(
    private val chatInteractor: ChatInteractor,
) : Actor<NewAppCommand, NewAppEvent> {

    override fun act(commands: Flow<NewAppCommand>): Flow<NewAppEvent> {
        return commands
            .filterIsInstance<NewAppCommand.LoadChatHistory>()
            .mapLatest { command ->
                runCatching { chatInteractor.getChatHistory(command.appId) }
                    .mapCatching(NewAppEvent::ChatHistoryLoaded)
                    .getOrElse { NewAppEvent.ChatHistoryError(it.message ?: "Failed to load chat history") }
            }
    }
}
