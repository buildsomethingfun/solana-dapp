package dapp.buildsomething.feature.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.style.AppTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.R


@Composable
internal fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit = { Unit },
) {
    LaunchedEffect(Unit) {
        delay(2.seconds)
        onAnimationFinished()
    }

    Box(
        modifier = modifier
            .background(AppTheme.Colors.Background.Primary)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(120.dp),
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.ic_app_logo_dark else R.drawable.ic_app_logo_gray
            ),
            contentDescription = null,
        )
    }
}

@AppPreview
@Composable
private fun Preview() {
    SplashScreenContent()
}
