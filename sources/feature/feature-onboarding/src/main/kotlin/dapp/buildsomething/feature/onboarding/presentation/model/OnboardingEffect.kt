package dapp.buildsomething.feature.onboarding.presentation.model

internal sealed interface OnboardingEffect {

    data object NavigateToAuth : OnboardingEffect

    data object NavigateToHome : OnboardingEffect

    data class ShowError(val message: String) : OnboardingEffect
}
