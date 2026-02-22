package dapp.buildsomething.common.navigation.core.toast

import dapp.buildsomething.common.navigation.core.Destination
import java.util.UUID

data class ToastItem(
    val id: String = UUID.randomUUID().toString(),
    val destination: Destination.Toast,
    val timestamp: Long = System.currentTimeMillis()
)