package dapp.buildsomething.feature.apps.presentation.model

import dapp.buildsomething.feature.apps.ui.AppUiModel

internal data class AppsState(
    val isLoading: Boolean = true,
    val apps: List<AppUiModel> = emptyList(),
    val error: String? = null,
)
