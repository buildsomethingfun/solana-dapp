package dapp.buildsomething.common.ui.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.R

@Composable
fun SuccessNotificationToast(
    message: String,
) {
    InAppNotificationToast(
        message = message,
        color = Color.Green,
        icon = painterResource(R.drawable.ds_check)
    )
}

@Composable
@AppPreview
private fun Preview() {
    SuccessNotificationToast(
        message = "Wallet successfully connected."
    )
}