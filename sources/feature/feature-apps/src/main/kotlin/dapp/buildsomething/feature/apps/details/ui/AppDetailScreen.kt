package dapp.buildsomething.feature.apps.details.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.feature.apps.details.presentation.AppDetailStore
import dapp.buildsomething.feature.apps.details.presentation.AppDetailStoreProvider
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEffect
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailUIEvent
import dapp.buildsomething.common.util.openUrl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.core.net.toUri

@Composable
fun AppDetailScreen(
    id: String,
    navigator: Navigator,
    storeProvider: AppDetailStoreProvider,
) {
    val store = viewModel<AppDetailStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(id) {
        store.accept(AppDetailUIEvent.LoadAppDetail(id))
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach { effect ->
                when (effect) {
                    is AppDetailEffect.ShowError -> {
                        navigator.open(AppDestination.ErrorToast(effect.message))
                    }
                    is AppDetailEffect.OpenUrl -> {
                        context.startActivity(Intent(Intent.ACTION_VIEW, effect.url.toUri()))
                    }
                    is AppDetailEffect.NavigateToEdit -> {
                        navigator.open(AppDestination.EditApp(appId = effect.id, appName = effect.name))
                    }
                    is AppDetailEffect.DeploySuccess -> {
                        navigator.open(AppDestination.SuccessToast("App deployed!"))
                        store.accept(AppDetailUIEvent.LoadAppDetail(id))
                    }
                    is AppDetailEffect.PublishCompleted -> {
                        navigator.open(AppDestination.SuccessToast("App published to dApp Store!"))
                        store.accept(AppDetailUIEvent.LoadAppDetail(id))
                    }
                }
            }
            .launchIn(this)
    }

    AppDetailScreenContent(
        state = state,
        onBackClick = { navigator.back() },
        onViewClick = { store.accept(AppDetailUIEvent.ViewApp) },
        onEditClick = { store.accept(AppDetailUIEvent.EditApp) },
        onPublishClick = { store.accept(AppDetailUIEvent.DeployApp) },
        onPublishToStoreClick = { store.accept(AppDetailUIEvent.PublishApp) },
        onRetryClick = { store.accept(AppDetailUIEvent.LoadAppDetail(id)) },
        onMintClick = { address -> context.openUrl("https://explorer.solana.com/address/$address") },
    )
}
