package dapp.buildsomething.feature.apps.app.ui

internal sealed interface AppsScreenState {
    data object Loading : AppsScreenState
    data object Error : AppsScreenState
    data object Empty : AppsScreenState
    data class Content(val apps: List<AppUiModel>) : AppsScreenState
}
