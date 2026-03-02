package dapp.buildsomething.feature.apps.app.presentation

import dapp.buildsomething.common.arch.tea.component.UiStateMapper
import dapp.buildsomething.feature.apps.app.presentation.model.AppsState
import dapp.buildsomething.feature.apps.app.ui.AppsScreenState

internal class AppsUiStateMapper : UiStateMapper<AppsState, AppsScreenState> {

    override fun map(state: AppsState): AppsScreenState {
        return when {
            state.isLoading -> AppsScreenState.Loading
            state.error != null -> AppsScreenState.Error
            state.apps.isEmpty() -> AppsScreenState.Empty
            else -> AppsScreenState.Content(state.apps)
        }
    }
}
