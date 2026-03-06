package dapp.buildsomething.feature.apps.details.presentation.model

internal sealed interface AppDetailEffect {
    data class ShowError(val message: String) : AppDetailEffect
    data class OpenUrl(val url: String) : AppDetailEffect
    data class NavigateToEdit(val id: String, val name: String) : AppDetailEffect
    data class DeploySuccess(val url: String) : AppDetailEffect
    data object PublishCompleted : AppDetailEffect
}
