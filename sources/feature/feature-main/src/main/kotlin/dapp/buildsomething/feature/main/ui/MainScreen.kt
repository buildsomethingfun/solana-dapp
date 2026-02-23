package dapp.buildsomething.feature.main.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.NavAnimation
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.navigation.core.destination
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.feature.apps.ui.AppDetailScreen
import dapp.buildsomething.feature.apps.ui.AppsScreen
import dapp.buildsomething.feature.main.ui.bottomnav.BottomNavBar
import dapp.buildsomething.feature.main.ui.bottomnav.Tab
import dapp.buildsomething.feature.main.ui.bottomnav.TabLifecycleOwner
import dapp.buildsomething.feature.main.ui.bottomnav.TabNavigator
import dapp.buildsomething.feature.newapp.ui.NewAppScreen
import dapp.buildsomething.feature.profile.ui.ProfileScreen
import org.koin.compose.koinInject

private val DefaultTab = Tab.Apps

@Composable
fun MainScreen(navigator: Navigator) {
    var selectedTab by rememberSaveable { mutableStateOf(DefaultTab) }

    val appsNavController = rememberNavController()
    val newAppNavController = rememberNavController()
    val profileNavController = rememberNavController()

    val navControllers = remember(appsNavController, newAppNavController, profileNavController) {
        mapOf(
            Tab.Apps to appsNavController,
            Tab.NewApp to newAppNavController,
            Tab.Profile to profileNavController,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Tab.entries.forEach { tab ->
                key(tab) {
                    TabNavHost(
                        tab = tab,
                        isSelected = tab == selectedTab,
                        navController = navControllers.getValue(tab),
                        globalNavigator = navigator,
                    )
                }
            }
        }

        BottomNavBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )
    }

    BackHandler {
        val currentNavController = navControllers.getValue(selectedTab)
        when {
            currentNavController.previousBackStackEntry != null -> {
                currentNavController.popBackStack()
            }
            selectedTab != DefaultTab -> {
                selectedTab = DefaultTab
            }
            else -> {
                navigator.back()
            }
        }
    }
}

@Composable
private fun TabNavHost(
    tab: Tab,
    isSelected: Boolean,
    navController: NavHostController,
    globalNavigator: Navigator,
) {
    val tabNavigator = remember(navController, globalNavigator) {
        TabNavigator(
            activity = globalNavigator.activity,
            tabNavController = navController,
            globalNavigator = globalNavigator,
        )
    }

    val parentLifecycle = LocalLifecycleOwner.current.lifecycle
    val tabLifecycleOwner = remember { TabLifecycleOwner() }

    DisposableEffect(parentLifecycle, isSelected) {
        val observer = LifecycleEventObserver { _, _ ->
            tabLifecycleOwner.updateState(parentLifecycle.currentState, isSelected)
        }
        parentLifecycle.addObserver(observer)
        tabLifecycleOwner.updateState(parentLifecycle.currentState, isSelected)
        onDispose { parentLifecycle.removeObserver(observer) }
    }

    CompositionLocalProvider(LocalLifecycleOwner provides tabLifecycleOwner) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(if (isSelected) 1f else 0f)
                .graphicsLayer { alpha = if (isSelected) 1f else 0f },
        ) {
            NavHost(
                navController = navController,
                startDestination = tab.startDestination,
            ) {
                tabDestinations(tab, tabNavigator)
            }
        }
    }
}

private fun NavGraphBuilder.tabDestinations(tab: Tab, navigator: Navigator) {
    when (tab) {
        Tab.Apps -> {
            destination<AppDestination.Apps> {
                AppsScreen(navigator = navigator, storeProvider = koinInject())
            }
            destination<AppDestination.AppDetail>(animate = NavAnimation.SlideRight) { args ->
                AppDetailScreen(id = args?.id.orEmpty(), navigator = navigator)
            }
        }
        Tab.NewApp -> destination<AppDestination.NewApp> { NewAppScreen(navigator, koinInject()) }
        Tab.Profile -> destination<AppDestination.Profile> { ProfileScreen(navigator) }
    }
}
