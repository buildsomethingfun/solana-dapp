package dapp.buildsomething.feature.auth.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.auth.presentation.model.AuthCommand as Command
import dapp.buildsomething.feature.auth.presentation.model.AuthEvent as Event
import dapp.buildsomething.repository.something.interactor.AuthInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class ConnectWalletActor(
    private val authInteractor: AuthInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.ConnectWallet>()
            .mapLatest {
                authInteractor.auth()
                    .mapCatching { Event.AuthSuccess }
                    .getOrElse { Event.AuthFailed(it.message.orEmpty()) }
            }
    }
}
