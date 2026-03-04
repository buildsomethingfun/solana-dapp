package dapp.buildsomething.feature.onboarding.presentation.model

internal sealed interface OnboardingCommand {

    data object LogOut : OnboardingCommand

    data class CreateProfile(val name: String, val email: String) : OnboardingCommand
}
