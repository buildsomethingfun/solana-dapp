package dapp.buildsomething.feature.onboarding.presentation.model

internal sealed interface OnboardingEvent {

    data object LogOutCompleted : OnboardingEvent

    data object ProfileCreated : OnboardingEvent

    data class ProfileCreationFailed(val message: String) : OnboardingEvent
}

internal sealed interface OnboardingUIEvent : OnboardingEvent {

    data class EmailChanged(val email: String) : OnboardingUIEvent

    data class NameChanged(val name: String) : OnboardingUIEvent

    data object UseDifferentWalletClicked : OnboardingUIEvent

    data object SubmitClicked : OnboardingUIEvent
}
