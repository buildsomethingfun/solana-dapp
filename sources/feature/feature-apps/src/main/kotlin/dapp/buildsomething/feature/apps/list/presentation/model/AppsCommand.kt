package dapp.buildsomething.feature.apps.app.presentation.model

internal sealed interface AppsCommand {
    data object LoadApps : AppsCommand
}
