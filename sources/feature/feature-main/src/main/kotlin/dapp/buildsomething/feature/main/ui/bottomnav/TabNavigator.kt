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
) : Navigator {

    override fun open(destination: Destination, options: NavigationBuilder.() -> Unit) {
        when (destination) {
            is Destination.Screen -> {
                tabNavController.navigate(destination) {
                    val config = NavigationBuilder().apply(options)
                    launchSingleTop = true
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
