package dapp.buildsomething.feature.splash.presentation.model

internal sealed interface SplashEvent {

    data class AuthStateLoaded(val isAuthenticated: Boolean) : SplashEvent
}

internal sealed interface SplashUIEvent : SplashEvent {

    data object LoadOnboardingState : SplashUIEvent
}