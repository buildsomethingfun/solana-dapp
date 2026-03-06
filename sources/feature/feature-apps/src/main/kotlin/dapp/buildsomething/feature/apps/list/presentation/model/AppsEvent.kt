package dapp.buildsomething.feature.apps.list.presentation.model

import dapp.buildsomething.feature.apps.list.ui.AppUiModel

internal sealed interface AppsEvent {
    data class AppsLoaded(val apps: List<AppUiModel>) : AppsEvent
    data class AppsLoadFailed(val message: String) : AppsEvent
}

internal sealed interface AppsUIEvent : AppsEvent {
    data object LoadApps : AppsUIEvent
}
