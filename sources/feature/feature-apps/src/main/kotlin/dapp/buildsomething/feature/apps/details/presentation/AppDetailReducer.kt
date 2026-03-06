package dapp.buildsomething.feature.apps.details.presentation

import dapp.buildsomething.common.arch.tea.dsl.DslReducer
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailCommand as Command
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEffect as Effect
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent as Event
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailState as State
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailUIEvent as UIEvent
import dapp.buildsomething.feature.apps.details.presentation.model.PublishingStep

internal class AppDetailReducer : DslReducer<Command, Effect, Event, State>() {

    override fun reduce(event: Event) {
        when (event) {
            is UIEvent.LoadAppDetail -> {
                state { copy(isLoading = app == null, error = null) }
                commands(Command.LoadAppDetail(event.id))
            }
            is UIEvent.ViewApp -> {
                val url = state.app?.deployedUrl ?: return
                effects(Effect.OpenUrl(url))
            }
            is UIEvent.EditApp -> {
                val app = state.app ?: return
                effects(Effect.NavigateToEdit(id = app.id, name = app.name))
            }
            is UIEvent.DeployApp -> {
                val id = state.app?.id ?: return
                state { copy(isDeploying = true) }
                commands(Command.DeployApp(id))
            }
            is UIEvent.PublishApp -> {
                val app = state.app ?: return
                if (state.publishingStep != null) return
                state { copy(publishingStep = PublishingStep.CreatingRelease) }
                commands(Command.PublishApp(app.id))
            }
            is Event.AppDetailLoaded -> {
                state { copy(isLoading = false, app = event.app) }
            }
            is Event.AppDetailLoadFailed -> {
                state { copy(isLoading = false, error = event.message) }
                effects(Effect.ShowError(event.message))
            }
            is Event.DeploySuccess -> {
                state {
                    copy(
                        isDeploying = false,
                        app = app?.copy(deployedUrl = event.url),
                    )
                }
                effects(Effect.DeploySuccess(event.url))
            }
            is Event.DeployFailed -> {
                state { copy(isDeploying = false) }
                effects(Effect.ShowError(event.message))
            }
            is Event.PublishStepUpdate -> {
                state { copy(publishingStep = event.step) }
            }
            is Event.PublishCompleted -> {
                state { copy(publishingStep = PublishingStep.Done) }
                effects(Effect.PublishCompleted)
            }
            is Event.PublishFailed -> {
                state { copy(publishingStep = null) }
                effects(Effect.ShowError(event.message))
            }
        }
    }
}
