package dapp.buildsomething.feature.apps.details.ui

import dapp.buildsomething.feature.apps.details.presentation.model.PublishingStep

internal sealed interface AppDetailScreenState {
    data object Loading : AppDetailScreenState
    data object Error : AppDetailScreenState
    data class Content(
        val app: AppDetailUiModel,
        val isDeploying: Boolean = false,
        val publishingStep: PublishingStep? = null,
    ) : AppDetailScreenState
}
