package dapp.buildsomething.feature.auth.presentation.model

internal sealed interface AuthCommand {

    data object ConnectWallet : AuthCommand
}
