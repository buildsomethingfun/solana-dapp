package dapp.buildsomething.common.ui.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.R

@Composable
fun ErrorNotificationToast(
    message: String,
) {
    InAppNotificationToast(
        message = message,
        color = Color.Red,
        icon = painterResource(R.drawable.ds_close)
    )
}

@Composable
@AppPreview
private fun Preview() {
    ErrorNotificationToast(
        message = "Could not connect your wallet. Try again later."
    )
}