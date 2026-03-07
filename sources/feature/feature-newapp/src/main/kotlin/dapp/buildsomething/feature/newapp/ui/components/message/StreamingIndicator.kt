package dapp.buildsomething.feature.newapp.ui.components.message

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Regular
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

private const val TextRotationMs = 6_000L
private const val ShapeRotationMs = 4_000L

private enum class Shape { CIRCLE, TRIANGLE, SQUARE, DIAMOND, PENTAGON, HEXAGON }

@Composable
internal fun StreamingIndicator(modifier: Modifier = Modifier) {
    var statusText by remember { mutableStateOf(LoadingStatuses.random()) }
    var shapeIndex by remember { mutableIntStateOf(0) }
    val shapes = remember { Shape.entries }

    LaunchedEffect(Unit) {
        while (true) {
            delay(TextRotationMs)
            statusText = LoadingStatuses.random()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(ShapeRotationMs)
            shapeIndex = (shapeIndex + 1) % shapes.size
        }
    }

    val currentShape = shapes[shapeIndex]

    val transition = rememberInfiniteTransition(label = "streaming")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    val color = AppTheme.Colors.Core.Accent

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 4.dp),
    ) {
        Canvas(modifier = Modifier.size(18.dp)) {
            rotate(rotation) {
                when (currentShape) {
                    Shape.CIRCLE -> drawSpinningCircle(color, pulse)
                    Shape.TRIANGLE -> drawPolygon(color, sides = 3, pulse)
                    Shape.SQUARE -> drawPolygon(color, sides = 4, pulse)
                    Shape.DIAMOND -> drawDiamond(color, pulse)
                    Shape.PENTAGON -> drawPolygon(color, sides = 5, pulse)
                    Shape.HEXAGON -> drawPolygon(color, sides = 6, pulse)
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$statusText...",
            style = Body14Regular.copy(brush = rememberStreamingShimmerBrush()),
        )
    }
}

private fun DrawScope.drawSpinningCircle(color: Color, pulse: Float) {
    val radius = size.minDimension / 2 * pulse
    drawCircle(
        color = color,
        radius = radius,
        style = Stroke(width = 2.dp.toPx()),
    )
    drawCircle(
        color = color,
        radius = 2.dp.toPx(),
        center = Offset(center.x + radius, center.y),
    )
}

private fun DrawScope.drawPolygon(color: Color, sides: Int, pulse: Float) {
    val radius = size.minDimension / 2 * pulse
    val path = Path().apply {
        for (i in 0 until sides) {
            val angle = Math.toRadians((360.0 / sides * i) - 90.0)
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    drawPath(path, color = color, style = Stroke(width = 2.dp.toPx()))
}

private fun DrawScope.drawDiamond(color: Color, pulse: Float) {
    val half = size.minDimension / 2 * pulse
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - half * 0.6f, center.y - half),
        size = Size(half * 1.2f, half * 2f),
        cornerRadius = CornerRadius(2.dp.toPx()),
        style = Stroke(width = 2.dp.toPx()),
    )
}

@Composable
private fun rememberStreamingShimmerBrush(): Brush {
    val color = AppTheme.Colors.Text.Primary
    val transition = rememberInfiniteTransition(label = "streamingShimmer")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
        ),
        label = "streamingShimmerOffset",
    )
    return Brush.linearGradient(
        colors = listOf(
            color.copy(alpha = 0.4f),
            color.copy(alpha = 1f),
            color.copy(alpha = 0.4f),
        ),
        start = Offset(offset - 150f, 0f),
        end = Offset(offset, 0f),
    )
}
