package dapp.buildsomething.feature.auth.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.components.button.PrimaryButton
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Heading1

@Composable
internal fun AuthScreenContent(
    isLoading: Boolean,
    onAuthClick: () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "cubes")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        FloatingCube(
            transition = transition,
            drawableRes = dapp.buildsomething.common.ui.R.drawable.ic_round_cube,
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-30).dp, y = 22.dp),
            floatAmplitudeY = 10f,
            floatAmplitudeX = 4f,
            durationY = 3200,
            durationX = 4800,
            blurMin = 0.dp,
            blurMax = 3.dp,
            blurDuration = 5000,
            appearDelay = 0L,
        )
        FloatingCube(
            transition = transition,
            drawableRes = dapp.buildsomething.common.ui.R.drawable.ic_round_cube_2,
            modifier = Modifier
                .size(110.dp)
                .offset(x = (-50).dp, y = 200.dp)
                .align(Alignment.CenterStart),
            floatAmplitudeY = 14f,
            floatAmplitudeX = 6f,
            durationY = 3800,
            durationX = 5400,
            blurMin = 1.dp,
            blurMax = 5.dp,
            blurDuration = 4200,
            appearDelay = 150L,
        )
        FloatingCube(
            transition = transition,
            drawableRes = dapp.buildsomething.common.ui.R.drawable.ic_round_cube_3,
            modifier = Modifier
                .size(120.dp, 136.dp)
                .offset(x = 40.dp, y = (-60).dp)
                .align(Alignment.CenterEnd),
            floatAmplitudeY = 8f,
            floatAmplitudeX = 5f,
            durationY = 4400,
            durationX = 3600,
            blurMin = 0.dp,
            blurMax = 4.dp,
            blurDuration = 6000,
            appearDelay = 300L,
        )

        val inlineIcon = mapOf(
            "dappStore" to InlineTextContent(
                placeholder = Placeholder(
                    width = 60.sp,
                    height = 60.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) {
                Image(
                    painter = painterResource(id = dapp.buildsomething.common.ui.R.drawable.ic_dapp_store),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            },
        )
        val headlineText = buildAnnotatedString {
            append("Build\n")
            appendInlineContent("dappStore")
            append("dApps\nwithout code")
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            style = Heading1.copy(lineHeight = 50.sp),
            color = AppTheme.Colors.Text.Primary,
            text = headlineText,
            inlineContent = inlineIcon,
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(
                text = "Connect Wallet",
                onClick = onAuthClick,
                loading = isLoading,
            )
            val termsText = buildAnnotatedString {
                append("By proceeding, you agree to\n")
                append("buildsomething.fun ")
                withStyle(SpanStyle(color = AppTheme.Colors.Text.Interactive)) {
                    append("Terms of Use")
                }
            }
            Text(
                text = termsText,
                style = Body14Regular,
                color = AppTheme.Colors.Text.Secondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun FloatingCube(
    transition: androidx.compose.animation.core.InfiniteTransition,
    drawableRes: Int,
    modifier: Modifier = Modifier,
    floatAmplitudeY: Float,
    floatAmplitudeX: Float,
    durationY: Int,
    durationX: Int,
    blurMin: Dp,
    blurMax: Dp,
    blurDuration: Int,
    appearDelay: Long = 0L,
) {
    val appear = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(appearDelay)
        appear.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )
    }

    val offsetY by transition.animateFloat(
        initialValue = -floatAmplitudeY,
        targetValue = floatAmplitudeY,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationY, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "floatY",
    )
    val offsetX by transition.animateFloat(
        initialValue = -floatAmplitudeX,
        targetValue = floatAmplitudeX,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationX, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "floatX",
    )
    val blurFraction by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = blurDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "blur",
    )
    val blurRadius = blurMin + (blurMax - blurMin) * blurFraction

    Image(
        modifier = modifier
            .graphicsLayer {
                val progress = appear.value
                alpha = progress
                scaleX = 0.6f + 0.4f * progress
                scaleY = 0.6f + 0.4f * progress
                translationY = offsetY + (1f - progress) * 40f
                translationX = offsetX
            }
            .blur(blurRadius),
        painter = painterResource(id = drawableRes),
        contentDescription = null,
    )
}

@Composable
@AppPreview
private fun Preview() {
    AuthScreenContent(
        isLoading = false,
    ) {}
}
