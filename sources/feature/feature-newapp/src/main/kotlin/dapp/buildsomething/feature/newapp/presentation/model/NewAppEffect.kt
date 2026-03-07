package dapp.buildsomething.feature.newapp.presentation.model

internal sealed interface NewAppEffect {
    data class ShowError(val message: String) : NewAppEffect
    data class NavigateToAppDetail(val appId: String) : NewAppEffect
}
