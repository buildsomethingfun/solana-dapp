package dapp.buildsomething.common.navigation

import dapp.buildsomething.common.navigation.core.Destination
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppDestination : Destination {

    @Serializable
    data object Splash : AppDestination, Destination.Screen

    @Serializable
    data object Auth : AppDestination, Destination.Screen

    @Serializable
    data object Home : AppDestination, Destination.Screen

    @Serializable
    data object Apps : AppDestination, Destination.Screen

    @Serializable
    data class AppDetail(val id: String) : AppDestination, Destination.Screen

    @Serializable
    data object NewApp : AppDestination, Destination.Screen

    @Serializable
    data class EditApp(val appId: String, val appName: String) : AppDestination, Destination.Screen

    @Serializable
    data object Onboarding : AppDestination, Destination.Screen

    @Serializable
    data object Profile : AppDestination, Destination.Screen

    @Serializable
    data class ErrorToast(val text: String) : AppDestination, Destination.Toast

    @Serializable
    data class SuccessToast(val text: String) : AppDestination, Destination.Toast
}
