package dapp.buildsomething.feature.profile.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.profile.presentation.model.ProfileCommand as Command
import dapp.buildsomething.feature.profile.presentation.model.ProfileEffect as Effect
import dapp.buildsomething.feature.profile.presentation.model.ProfileEvent as Event
import dapp.buildsomething.feature.profile.presentation.model.ProfileState as State
import dapp.buildsomething.feature.profile.presentation.model.ProfileUIEvent as UIEvent

internal class ProfileReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent.LoadProfile -> {
                state { copy(isLoading = true) }
                commands(Command.LoadProfile)
            }
            is UIEvent.SignOutClicked -> {
                commands(Command.SignOut)
            }
            is Event.ProfileLoaded -> {
                state {
                    copy(
                        isLoading = false,
                        displayName = event.publisher.displayName.orEmpty(),
                        email = event.publisher.contactEmail.orEmpty(),
                        walletAddress = event.publisher.walletAddress,
                        avatarUrl = event.publisher.avatarUrl,
                    )
                }
            }
            is Event.ProfileLoadFailed -> {
                state { copy(isLoading = false) }
                effects(Effect.ShowError(event.message))
            }
            is Event.LogOutCompleted -> {
                effects(Effect.NavigateToAuth)
            }
        }
    }
}
