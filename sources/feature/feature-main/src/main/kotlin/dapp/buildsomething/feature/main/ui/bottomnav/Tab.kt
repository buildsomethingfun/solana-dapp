package dapp.buildsomething.feature.main.ui.bottomnav

import androidx.annotation.DrawableRes
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.Destination
import dapp.buildsomething.common.ui.R

internal enum class Tab(
    val label: String,
    val startDestination: Destination.Screen,
    @DrawableRes val icon: Int,
) {
    Apps("Apps", AppDestination.Apps, R.drawable.ic_home),
    NewApp("New App", AppDestination.NewApp, R.drawable.ic_sparkles),
    Profile("Profile", AppDestination.Profile, R.drawable.ic_user),
}
