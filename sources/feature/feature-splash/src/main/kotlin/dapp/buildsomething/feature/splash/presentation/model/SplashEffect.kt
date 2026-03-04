package dapp.buildsomething.feature.splash.presentation.model

internal sealed interface SplashEffect {

    data object OpenOnboarding : SplashEffect

    data object OpenProfileOnboarding : SplashEffect

    data object OpenHome : SplashEffect
}
