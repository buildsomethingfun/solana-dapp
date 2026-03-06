package dapp.buildsomething.feature.apps.list.presentation.model

internal sealed interface AppsCommand {
    data object LoadApps : AppsCommand
}
