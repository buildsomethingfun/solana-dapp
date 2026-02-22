package dapp.buildsomething.feature.apps.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.ui.modifier.bouncingClickable
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16SemiBold
import dapp.buildsomething.common.ui.style.Heading3

private val mockApps = listOf("alpha", "beta", "gamma", "delta", "epsilon")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsScreen(navigator: Navigator) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Apps",
                style = Heading3,
                color = AppTheme.Colors.Text.Primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        items(mockApps) { id ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppTheme.Colors.Background.Surface)
                    .bouncingClickable { navigator.open(AppDestination.AppDetail(id)) }
                    .padding(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "App $id",
                        style = Body16SemiBold,
                        color = AppTheme.Colors.Text.Primary,
                    )
                    Text(
                        text = "Tap to open detail",
                        style = Body14Regular,
                        color = AppTheme.Colors.Text.Secondary,
                    )
                }
            }
        }
    }
}
