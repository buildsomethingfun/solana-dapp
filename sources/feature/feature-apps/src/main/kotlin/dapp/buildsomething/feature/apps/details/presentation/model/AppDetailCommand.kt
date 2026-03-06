package dapp.buildsomething.feature.apps.details.presentation.model

internal sealed interface AppDetailCommand {
    data class LoadAppDetail(val id: String) : AppDetailCommand
    data class DeployApp(val id: String) : AppDetailCommand
    data class PublishApp(val appId: String) : AppDetailCommand
}
