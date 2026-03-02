package dapp.buildsomething.feature.apps.app.presentation.model

internal sealed interface AppsEffect {
    data class ShowError(val message: String) : AppsEffect
}
