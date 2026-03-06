package dapp.buildsomething.feature.apps.details.presentation.model

import dapp.buildsomething.feature.apps.details.ui.AppDetailUiModel

internal data class AppDetailState(
    val isLoading: Boolean = true,
    val isDeploying: Boolean = false,
    val app: AppDetailUiModel? = null,
    val error: String? = null,
    val publishingStep: PublishingStep? = null,
)
