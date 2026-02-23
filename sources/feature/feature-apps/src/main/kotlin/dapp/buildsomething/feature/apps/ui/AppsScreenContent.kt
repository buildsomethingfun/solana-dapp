package dapp.buildsomething.feature.apps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.components.button.AppTextButton
import dapp.buildsomething.common.ui.components.button.PillButton
import dapp.buildsomething.common.ui.components.button.PrimaryButton
import dapp.buildsomething.common.ui.rememberAppShimmer
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16Regular
import dapp.buildsomething.common.ui.style.Heading2
import dapp.buildsomething.common.ui.style.Heading3
import dapp.buildsomething.common.ui.style.Heading4
import dapp.buildsomething.common.ui.R as CommonUiR

@Composable
internal fun AppsScreenContent(
    state: AppsScreenState,
    onRetryClick: () -> Unit,
    onCreateClick: () -> Unit,
    onAppClick: (String) -> Unit,
) {
    when (state) {
        AppsScreenState.Loading -> AppsLoading()
        AppsScreenState.Error -> AppsError(onRetryClick = onRetryClick)
        AppsScreenState.Empty -> AppsEmpty(onCreateClick = onCreateClick)
        is AppsScreenState.Content -> AppsContent(apps = state.apps, onAppClick = onAppClick)
    }
}

@Composable
private fun AppsLoading() {
    val shimmer = rememberAppShimmer()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .shimmer(shimmer)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(width = 80.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AppTheme.Colors.Background.Tertiary),
        )
        repeat(5) {
            AppItemSkeleton()
        }
    }
}

@Composable
private fun AppItemSkeleton() {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppTheme.Colors.Background.Tertiary),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
                Box(
                    modifier = Modifier
                        .size(width = 180.dp, height = 14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(width = 64.dp, height = 36.dp)
                        .clip(RoundedCornerShape(48.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = AppTheme.Colors.Border.Primary)
    }
}

@Composable
private fun AppsError(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Oops...",
            style = Heading2,
            color = AppTheme.Colors.Text.Primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We're having trouble fetching your apps right now",
            style = Body16Regular,
            color = AppTheme.Colors.Text.Secondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        AppTextButton(
            text = "Try again",
            onClick = onRetryClick,
            iconStart = CommonUiR.drawable.ic_retry,
        )
    }
}

@Composable
private fun AppsEmpty(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "No dApps yet",
            style = Heading2,
            color = AppTheme.Colors.Text.Primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create, preview, and publish apps to the Solana dApp Store",
            style = Body16Regular,
            color = AppTheme.Colors.Text.Secondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Create dApp",
            onClick = onCreateClick,
        )
    }
}

@Composable
private fun AppsContent(
    apps: List<AppUiModel>,
    onAppClick: (String) -> Unit,
) {
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
        items(apps, key = { it.id }) { app ->
            AppItem(app = app, onViewClick = { onAppClick(app.id) })
        }
    }
}

@Composable
private fun AppItem(
    app: AppUiModel,
    onViewClick: () -> Unit,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            AppImage(imageUrl = app.imageUrl)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = app.name,
                    style = Heading4,
                    color = AppTheme.Colors.Text.Primary,
                )
                Text(
                    text = app.description,
                    style = Body14Regular,
                    color = AppTheme.Colors.Text.Secondary,
                )
                Spacer(modifier = Modifier.height(12.dp))
                PillButton(
                    text = "View",
                    onClick = onViewClick,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = AppTheme.Colors.Border.Primary)
    }
}

@AppPreview
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        AppsScreenContent(
            state = AppsScreenState.Content(
                apps = listOf(
                    AppUiModel("1", "Jupiter", "Swap aggregator on Solana", null),
                    AppUiModel("2", "Tensor", "NFT marketplace and trading platform", null),
                ),
            ),
            onRetryClick = {},
            onCreateClick = {},
            onAppClick = {},
        )
    }
}
