package dapp.buildsomething.feature.apps.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.feature.apps.app.presentation.AppsStore
import dapp.buildsomething.feature.apps.app.presentation.AppsStoreProvider
import dapp.buildsomething.feature.apps.app.presentation.model.AppsEffect
import dapp.buildsomething.feature.apps.app.presentation.model.AppsUIEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AppsScreen(
    navigator: Navigator,
    storeProvider: AppsStoreProvider,
) {
    val store = viewModel<AppsStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()

    LifecycleEventEffect(ON_RESUME) {
        store.accept(AppsUIEvent.LoadApps)
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach { effect ->
                when (effect) {
                    is AppsEffect.ShowError -> {
                        navigator.open(AppDestination.ErrorToast(effect.message))
                    }
                }
            }
            .launchIn(this)
    }

    AppsScreenContent(
        state = state,
        onRetryClick = { store.accept(AppsUIEvent.LoadApps) },
        onCreateClick = { navigator.open(AppDestination.NewApp) },
        onAppClick = { id -> navigator.open(AppDestination.AppDetail(id)) },
    )
}
