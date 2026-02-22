package dapp.buildsomething.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.NavigationOption
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.feature.auth.presentation.AuthStore
import dapp.buildsomething.feature.auth.presentation.AuthStoreProvider
import dapp.buildsomething.feature.auth.presentation.model.AuthEffect
import dapp.buildsomething.feature.auth.presentation.model.AuthUIEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AuthScreen(
    navigator: Navigator,
    storeProvider: AuthStoreProvider,
) {
    val store = viewModel<AuthStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()

    BackHandler(onBack = navigator::back)

    fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is AuthEffect.NavigateToHome -> {
                navigator.open(AppDestination.Home) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
            is AuthEffect.ShowError -> {
                navigator.open(AppDestination.ErrorToast(effect.message))
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach(::handleEffect)
            .launchIn(this)
    }

    AuthScreenContent(
        isLoading = state.isLoading,
        onAuthClick = { store.accept(AuthUIEvent.OnConnectWalletClicked) },
    )
}
