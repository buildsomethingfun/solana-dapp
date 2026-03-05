package dapp.buildsomething.feature.main.ui.bottomnav

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import dapp.buildsomething.common.navigation.core.Destination
import dapp.buildsomething.common.navigation.core.NavigationBuilder
import dapp.buildsomething.common.navigation.core.NavigationOption
import dapp.buildsomething.common.navigation.core.Navigator

internal class TabNavigator(
    override val activity: AppCompatActivity,
    private val tabNavController: NavController,
    private val globalNavigator: Navigator,
    private val onSwitchTab: (Tab) -> Unit,
) : Navigator {

    override fun open(destination: Destination, options: NavigationBuilder.() -> Unit) {
        when (destination) {
            is Destination.Screen -> {
                try {
                    tabNavController.navigate(destination) {
                        val config = NavigationBuilder().apply(options)
                        launchSingleTop = true
                    }
                } catch (_: IllegalArgumentException) {
                    val targetTab = Tab.entries.find { it.startDestination == destination }
                    if (targetTab != null) {
                        onSwitchTab(targetTab)
                    } else {
                        globalNavigator.open(destination, options)
                    }
                }
            }
            else -> globalNavigator.open(destination, options)
        }
    }

    override fun back() {
        if (tabNavController.previousBackStackEntry != null) {
            tabNavController.popBackStack()
        } else {
            globalNavigator.back()
        }
    }
}
