package dapp.buildsomething.common.navigation.core.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dapp.buildsomething.common.navigation.core.Destination
import dapp.buildsomething.common.navigation.core.Navigator

@Composable
fun DialogOverlay(
    dialogState: DialogState,
    navigator: Navigator,
    content: @Composable (Destination.Dialog, Navigator) -> Unit
) {
    val currentDialog by dialogState.currentDialog.collectAsState()

    currentDialog?.let { dialog ->
        content(dialog, navigator)
    }
}