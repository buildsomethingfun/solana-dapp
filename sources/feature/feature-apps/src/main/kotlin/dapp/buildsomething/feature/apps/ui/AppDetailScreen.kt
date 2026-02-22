package dapp.buildsomething.feature.apps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body16Regular
import dapp.buildsomething.common.ui.style.Heading3

@Composable
fun AppDetailScreen(
    id: String,
    navigator: Navigator,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "App Detail",
            style = Heading3,
            color = AppTheme.Colors.Text.Primary,
        )
        Text(
            text = "id: $id",
            style = Body16Regular,
            color = AppTheme.Colors.Text.Secondary,
        )
    }
}
