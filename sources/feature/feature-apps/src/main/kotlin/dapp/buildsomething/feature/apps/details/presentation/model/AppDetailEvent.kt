package dapp.buildsomething.feature.apps.details.presentation.model

import dapp.buildsomething.feature.apps.details.ui.AppDetailUiModel

internal sealed interface AppDetailEvent {
    data class AppDetailLoaded(val app: AppDetailUiModel) : AppDetailEvent
    data class AppDetailLoadFailed(val message: String) : AppDetailEvent
    data class DeploySuccess(val url: String) : AppDetailEvent
    data class DeployFailed(val message: String) : AppDetailEvent

    data class PublishStepUpdate(val step: PublishingStep) : AppDetailEvent
    data object PublishCompleted : AppDetailEvent
    data class PublishFailed(val message: String) : AppDetailEvent
}

internal sealed interface AppDetailUIEvent : AppDetailEvent {
    data class LoadAppDetail(val id: String) : AppDetailUIEvent
    data object ViewApp : AppDetailUIEvent
    data object EditApp : AppDetailUIEvent
    data object DeployApp : AppDetailUIEvent
    data object PublishApp : AppDetailUIEvent
}
