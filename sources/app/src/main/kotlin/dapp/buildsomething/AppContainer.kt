package dapp.buildsomething

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.AppNavigation
import dapp.buildsomething.common.navigation.core.Destination
import dapp.buildsomething.common.navigation.core.NavAnimation
import dapp.buildsomething.common.navigation.core.destination
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.feature.apps.details.ui.AppDetailScreen
import dapp.buildsomething.feature.auth.ui.AuthScreen
import dapp.buildsomething.feature.main.ui.MainScreen
import dapp.buildsomething.feature.newapp.ui.NewAppScreen
import dapp.buildsomething.feature.onboarding.ui.OnboardingScreen
import dapp.buildsomething.feature.splash.ui.SplashScreen
import org.koin.compose.koinInject
import dapp.buildsomething.common.ui.components.toast.ErrorNotificationToast
import dapp.buildsomething.common.ui.components.toast.SuccessNotificationToast

@Composable
internal fun AppContainer(
    activity: AppCompatActivity,
    startDestination: Destination.Screen = AppDestination.Splash,
) {
    Box(
        modifier = Modifier
            .background(AppTheme.Colors.Background.Primary)
            .fillMaxSize()
    ) {
        AppNavigation(
            activity = activity,
            startDestination = startDestination,
            destinations = { navigator ->
                destination<AppDestination.Splash> {
                    SplashScreen(
                        navigator = navigator,
                        storeProvider = koinInject(),
                    )
                }
                destination<AppDestination.Auth> {
                    AuthScreen(
                        navigator = navigator,
                        storeProvider = koinInject(),
                    )
                }
                destination<AppDestination.Onboarding> {
                    OnboardingScreen(
                        navigator = navigator,
                        storeProvider = koinInject(),
                    )
                }
                destination<AppDestination.Home> {
                    MainScreen(navigator = navigator)
                }
                destination<AppDestination.AppDetail>(animate = NavAnimation.SlideRight) { args ->
                    AppDetailScreen(
                        id = args?.id.orEmpty(),
                        navigator = navigator,
                        storeProvider = koinInject(),
                    )
                }
                destination<AppDestination.EditApp>(animate = NavAnimation.SlideRight) { args ->
                    NewAppScreen(
                        navigator = navigator,
                        storeProvider = koinInject(),
                        appId = args?.appId,
                        appName = args?.appName,
                    )
                }
            },
            sheets = { sheet, navigator -> },
            dialogs = { dialog, navigator -> },
            overlayScreens = { overlay, navigator -> },
            toasts = { toast, navigator ->
                when (toast) {
                    is AppDestination.ErrorToast -> {
                        ErrorNotificationToast(toast.text)
                    }
                    is AppDestination.SuccessToast -> {
                        SuccessNotificationToast(toast.text)
                    }
                }
            }
        )
    }
}
