package dapp.buildsomething.feature.apps.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.apps.presentation.model.AppsCommand as Command
import dapp.buildsomething.feature.apps.presentation.model.AppsEffect as Effect
import dapp.buildsomething.feature.apps.presentation.model.AppsEvent as Event
import dapp.buildsomething.feature.apps.presentation.model.AppsState as State
import dapp.buildsomething.feature.apps.presentation.model.AppsUIEvent as UIEvent

internal class AppsReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent.LoadApps -> {
                state { copy(isLoading = true, error = null) }
                commands(Command.LoadApps)
            }
            is Event.AppsLoaded -> {
                state { copy(isLoading = false, apps = event.apps) }
            }
            is Event.AppsLoadFailed -> {
                state { copy(isLoading = false, error = event.message) }
            }
        }
    }
}
