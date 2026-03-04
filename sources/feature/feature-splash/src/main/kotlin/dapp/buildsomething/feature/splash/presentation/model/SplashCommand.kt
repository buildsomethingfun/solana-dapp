package dapp.buildsomething.feature.splash.presentation.model

internal sealed interface SplashCommand {

    data object LoadOnboardingState : SplashCommand

    data object CheckOnboardingStatus : SplashCommand
}
