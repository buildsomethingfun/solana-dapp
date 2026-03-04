package dapp.buildsomething.feature.onboarding.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingCommand as Command
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEffect as Effect
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEvent as Event
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingState as State
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingUIEvent as UIEvent

internal class OnboardingReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent.EmailChanged -> {
                state { copy(email = event.email) }
            }
            is UIEvent.NameChanged -> {
                state { copy(name = event.name) }
            }
            is UIEvent.UseDifferentWalletClicked -> {
                commands(Command.LogOut)
            }
            is UIEvent.SubmitClicked -> {
                state { copy(isLoading = true) }
                commands(Command.CreateProfile(name = state.name, email = state.email))
            }
            is Event.LogOutCompleted -> {
                effects(Effect.NavigateToAuth)
            }
            is Event.ProfileCreated -> {
                state { copy(isLoading = false) }
                effects(Effect.NavigateToHome)
            }
            is Event.ProfileCreationFailed -> {
                state { copy(isLoading = false) }
                effects(Effect.ShowError(event.message))
            }
        }
    }
}
