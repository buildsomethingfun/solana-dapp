package dapp.buildsomething.feature.splash.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.splash.presentation.model.SplashCommand as Command
import dapp.buildsomething.feature.splash.presentation.model.SplashEffect as Effect
import dapp.buildsomething.feature.splash.presentation.model.SplashEvent as Event
import dapp.buildsomething.feature.splash.presentation.model.SplashState as State
import dapp.buildsomething.feature.splash.presentation.model.SplashUIEvent as UIEvent

internal class SplashReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent -> reduceUI(event)
            else -> reduceEvent(event)
        }
    }

    private fun reduceUI(event: UIEvent) {
        when (event) {
            is UIEvent.LoadOnboardingState -> commands(Command.LoadOnboardingState)
        }
    }

    private fun reduceEvent(event: Event) {
        when (event) {
            is Event.AuthStateLoaded -> {
                if (event.isAuthenticated) {
                    effects(Effect.OpenHome)
                } else {
                    effects(Effect.OpenOnboarding)
                }
            }
            else -> Unit
        }
    }
}