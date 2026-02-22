package dapp.buildsomething.feature.splash.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.NavigationOption
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.feature.splash.presentation.SplashStore
import dapp.buildsomething.feature.splash.presentation.SplashStoreProvider
import dapp.buildsomething.feature.splash.presentation.model.SplashEffect
import dapp.buildsomething.feature.splash.presentation.model.SplashUIEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SplashScreen(
    navigator: Navigator,
    storeProvider: SplashStoreProvider,
) {
    val store = viewModel<SplashStore>(factory = storeProvider.viewModelFactory())

    BackHandler(onBack = navigator::back)

    fun handleEffect(effect: SplashEffect) {
        when (effect) {
            is SplashEffect.OpenHome -> {
                navigator.open(AppDestination.Home) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
            is SplashEffect.OpenOnboarding -> {
                navigator.open(AppDestination.Auth) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach(::handleEffect)
            .launchIn(this)
    }

    SplashScreenContent {
        store.accept(SplashUIEvent.LoadOnboardingState)
    }
}