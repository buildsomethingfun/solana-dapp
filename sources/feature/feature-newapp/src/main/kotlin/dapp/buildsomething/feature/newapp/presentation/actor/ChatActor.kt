package dapp.buildsomething.feature.newapp.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.newapp.presentation.model.NewAppCommand
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEvent
import dapp.buildsomething.repository.something.interactor.ChatInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class ChatActor(
    private val chatInteractor: ChatInteractor,
) : Actor<NewAppCommand, NewAppEvent> {

    override fun act(commands: Flow<NewAppCommand>): Flow<NewAppEvent> {
        return commands
            .filterIsInstance<NewAppCommand.SendMessage>()
            .flatMapLatest { command ->
                flow {
                    emitAll(
                        chatInteractor.sendMessage(command.appId, command.text)
                            .map(NewAppEvent::StreamUpdate)
                    )
                    emit(NewAppEvent.StreamCompleted)
                }.catch { emit(NewAppEvent.StreamError(it.message ?: "Unknown error")) }
            }
    }
}
