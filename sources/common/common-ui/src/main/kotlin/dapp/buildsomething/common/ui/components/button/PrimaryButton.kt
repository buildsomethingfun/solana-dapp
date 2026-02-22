package dapp.buildsomething.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.style.*

private val ButtonShape = RoundedCornerShape(24.dp)
private val InnerShadowColor = Color(0xFFEDC049).copy(alpha = 0.6f)

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes iconStart: Int? = null,
    @DrawableRes iconEnd: Int? = null,
) {
    val accentColor = AppTheme.Colors.Core.Accent

    Surface(
        onClick = { if (enabled && !loading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .then(
                if (enabled) {
                    Modifier.innerShadow(
                        shape = ButtonShape,
                        color = InnerShadowColor,
                        blur = 12.dp,
                    )
                } else {
                    Modifier
                }
            ),
        enabled = enabled && !loading,
        shape = ButtonShape,
        color = if (enabled) accentColor else accentColor.copy(alpha = 0.5f),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                iconStart?.let { icon ->
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = icon),
                        tint = Color.White,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = Body20SemiBold.applyColor(
                        if (enabled) Color.White else Color.White.copy(alpha = 0.4f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                    textAlign = TextAlign.Center,
                )

                iconEnd?.let { icon ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = icon),
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PrimaryButton(
            text = "Continue",
            onClick = {},
        )

        PrimaryButton(
            text = "Loading...",
            onClick = {},
            loading = true,
        )

        PrimaryButton(
            text = "Disabled",
            onClick = {},
            enabled = false,
        )
    }
}
