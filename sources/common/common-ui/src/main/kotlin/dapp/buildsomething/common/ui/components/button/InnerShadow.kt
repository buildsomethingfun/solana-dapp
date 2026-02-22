package dapp.buildsomething.common.ui.components.button

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.innerShadow(
    shape: Shape,
    color: Color,
    blur: Dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
) = drawWithContent {
    drawContent()

    val shapePath = Path().apply {
        addOutline(shape.createOutline(size, layoutDirection, this@drawWithContent))
    }

    clipPath(shapePath) {
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                this.color = color.toArgb()
                if (blur > 0.dp) {
                    maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
                }
            }

            val spread = blur.toPx()
            val outerRect = android.graphics.RectF(
                -spread, -spread,
                size.width + spread, size.height + spread
            )

            val outerPath = android.graphics.Path().apply {
                addRect(outerRect, android.graphics.Path.Direction.CW)
            }

            val innerPath = shapePath.asAndroidPath()

            outerPath.op(innerPath, android.graphics.Path.Op.DIFFERENCE)
            outerPath.offset(offsetX.toPx(), offsetY.toPx())

            canvas.nativeCanvas.drawPath(outerPath, paint)
        }
    }
}
