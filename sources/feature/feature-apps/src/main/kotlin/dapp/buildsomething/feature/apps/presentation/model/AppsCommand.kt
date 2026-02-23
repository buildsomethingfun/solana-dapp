package dapp.buildsomething.feature.apps.presentation.model

internal sealed interface AppsCommand {
    data object LoadApps : AppsCommand
}
