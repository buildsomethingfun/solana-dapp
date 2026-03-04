package dapp.buildsomething.feature.onboarding.presentation.model

internal data class OnboardingState(
    val email: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
)
