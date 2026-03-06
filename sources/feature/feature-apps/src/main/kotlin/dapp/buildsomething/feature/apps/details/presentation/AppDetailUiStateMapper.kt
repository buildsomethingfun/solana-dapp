package dapp.buildsomething.feature.apps.details.presentation

import dapp.buildsomething.common.arch.tea.component.UiStateMapper
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailState
import dapp.buildsomething.feature.apps.details.ui.AppDetailScreenState

internal class AppDetailUiStateMapper : UiStateMapper<AppDetailState, AppDetailScreenState> {

    override fun map(state: AppDetailState): AppDetailScreenState {
        return when {
            state.isLoading -> AppDetailScreenState.Loading
            state.error != null && state.app == null -> AppDetailScreenState.Error
            state.app != null -> AppDetailScreenState.Content(
                app = state.app,
                isDeploying = state.isDeploying,
                publishingStep = state.publishingStep,
            )
            else -> AppDetailScreenState.Error
        }
    }
}
