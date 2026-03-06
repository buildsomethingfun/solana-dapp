package dapp.buildsomething.feature.apps.list.presentation.model

internal sealed interface AppsEffect {
    data class ShowError(val message: String) : AppsEffect
}
