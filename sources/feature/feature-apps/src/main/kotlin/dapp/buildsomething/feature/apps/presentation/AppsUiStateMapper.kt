package dapp.buildsomething.feature.apps.presentation

import dapp.buildsomething.common.arch.tea.component.UiStateMapper
import dapp.buildsomething.feature.apps.presentation.model.AppsState
import dapp.buildsomething.feature.apps.ui.AppsScreenState

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
