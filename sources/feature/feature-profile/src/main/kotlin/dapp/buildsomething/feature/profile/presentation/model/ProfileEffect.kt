package dapp.buildsomething.feature.profile.presentation.model

internal sealed interface ProfileEffect {

    data object NavigateToAuth : ProfileEffect

    data class ShowError(val message: String) : ProfileEffect
}
