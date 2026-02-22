package dapp.buildsomething.feature.newapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Heading2

@Composable
fun NewAppScreen(navigator: Navigator) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "New App",
            style = Heading2,
            color = AppTheme.Colors.Text.Primary,
        )
    }
}
