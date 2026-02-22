package dapp.buildsomething.feature.auth.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.auth.presentation.model.AuthCommand as Command
import dapp.buildsomething.feature.auth.presentation.model.AuthEffect as Effect
import dapp.buildsomething.feature.auth.presentation.model.AuthEvent as Event
import dapp.buildsomething.feature.auth.presentation.model.AuthState as State
import dapp.buildsomething.feature.auth.presentation.model.AuthUIEvent as UIEvent

internal class AuthReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent.OnConnectWalletClicked -> {
                state { copy(isLoading = true) }
                commands(Command.ConnectWallet)
            }
            is Event.AuthSuccess -> {
                state { copy(isLoading = false) }
                effects(Effect.NavigateToHome)
            }
            is Event.AuthFailed -> {
                state { copy(isLoading = false) }
                effects(Effect.ShowError(event.message))
            }
        }
    }
}
