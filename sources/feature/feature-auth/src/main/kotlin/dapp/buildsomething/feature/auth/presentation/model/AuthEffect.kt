package dapp.buildsomething.feature.auth.presentation.model

internal sealed interface AuthEffect {

    data object NavigateToHome : AuthEffect

    data object NavigateToOnboarding : AuthEffect

    data class ShowError(val message: String) : AuthEffect
}
