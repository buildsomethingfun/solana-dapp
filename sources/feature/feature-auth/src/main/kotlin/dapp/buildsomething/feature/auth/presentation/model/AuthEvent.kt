package dapp.buildsomething.feature.auth.presentation.model

internal sealed interface AuthEvent {

    data object AuthSuccess : AuthEvent

    data class AuthFailed(val message: String) : AuthEvent
}

internal sealed interface AuthUIEvent : AuthEvent {

    data object OnConnectWalletClicked : AuthUIEvent
}
