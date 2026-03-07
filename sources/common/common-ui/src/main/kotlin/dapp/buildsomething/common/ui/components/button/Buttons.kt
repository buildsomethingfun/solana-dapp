package dapp.buildsomething.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.R
import dapp.buildsomething.common.ui.style.*

@Composable
fun OutlineIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .width(40.dp)
            .height(32.dp)
            .background(
                color = AppTheme.Colors.Background.Primary,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = AppTheme.Colors.Border.Secondary,
                shape = RoundedCornerShape(4.dp),
            ),
        enabled = enabled,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            tint = AppTheme.Colors.Text.Tertiary,
            contentDescription = null,
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes iconStart: Int? = null,
    @DrawableRes iconEnd: Int? = null,
) {
    val accentColor = AppTheme.Colors.Core.Accent
    val contentAlpha = if (enabled) 1f else 0.4f

    Surface(
        onClick = { if (enabled && !loading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                width = 1.5.dp,
                color = accentColor.copy(alpha = contentAlpha),
                shape = RoundedCornerShape(24.dp),
            ),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = accentColor,
                    strokeWidth = 2.dp,
                )
            } else {
                iconStart?.let { icon ->
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = icon),
                        tint = accentColor.copy(alpha = contentAlpha),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = Body20SemiBold.applyColor(accentColor.copy(alpha = contentAlpha)),
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                    textAlign = TextAlign.Center,
                )

                iconEnd?.let { icon ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = icon),
                        tint = accentColor.copy(alpha = contentAlpha),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes iconStart: Int? = null,
    @DrawableRes iconEnd: Int? = null,
) {
    val accentColor = AppTheme.Colors.Core.Accent
    val contentAlpha = if (enabled) 1f else 0.4f

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) {
        iconStart?.let { icon ->
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                tint = accentColor.copy(alpha = contentAlpha),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = text,
            style = Body16SemiBold.applyColor(accentColor.copy(alpha = contentAlpha)),
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            textAlign = TextAlign.Center,
        )

        iconEnd?.let { icon ->
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                tint = accentColor.copy(alpha = contentAlpha),
                contentDescription = null,
            )
        }
    }
}

enum class PillButtonStyle {
    Primary,
    Outline,
    Dark,
}

@Composable
fun PillButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    style: PillButtonStyle = PillButtonStyle.Dark,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes iconStart: Int? = null,
    @DrawableRes iconEnd: Int? = null,
) {
    val accentColor = AppTheme.Colors.Core.Accent

    val containerColor = when (style) {
        PillButtonStyle.Primary -> accentColor
        PillButtonStyle.Outline -> Color.Transparent
        PillButtonStyle.Dark -> Color.Black
    }

    val contentColor = when (style) {
        PillButtonStyle.Primary -> Color.White
        PillButtonStyle.Outline -> accentColor
        PillButtonStyle.Dark -> Color.White
    }

    val borderModifier = if (style == PillButtonStyle.Outline) {
        Modifier.border(
            width = 1.dp,
            color = if (enabled) accentColor else accentColor.copy(alpha = 0.5f),
            shape = RoundedCornerShape(48.dp),
        )
    } else {
        Modifier
    }

    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Button(
            onClick = onClick,
            modifier = modifier
                .height(36.dp)
                .then(borderModifier),
            enabled = enabled && !loading,
            shape = RoundedCornerShape(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = if (style == PillButtonStyle.Outline) Color.Transparent else containerColor.copy(alpha = 0.5f),
                disabledContentColor = contentColor.copy(alpha = 0.5f),
            ),
            contentPadding = PaddingValues(horizontal = 16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp,
            ),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = contentColor,
                    strokeWidth = 2.dp,
                )
            } else {
                iconStart?.let { icon ->
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = icon),
                        tint = contentColor,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(
                    text = text,
                    style = Body14SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                iconEnd?.let { icon ->
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = icon),
                        tint = contentColor,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.Colors.Background.Primary)
            .padding(horizontal = 24.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrimaryButton(
            text = "Primary Button",
            onClick = {},
        )

        SecondaryButton(
            text = "Secondary Button",
            onClick = {},
        )

        AppTextButton(
            text = "Text Button",
            onClick = {},
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PillButton(
                text = "Primary",
                onClick = {},
                style = PillButtonStyle.Primary,
            )

            PillButton(
                text = "Outline",
                onClick = {},
                style = PillButtonStyle.Outline,
            )

            PillButton(
                text = "Dark",
                onClick = {},
                style = PillButtonStyle.Dark,
            )
        }

        OutlineIconButton(
            icon = R.drawable.ic_solana,
            onClick = {},
        )

        PrimaryButton(
            text = "Disabled",
            onClick = {},
            enabled = false,
        )

        SecondaryButton(
            text = "Disabled Outline",
            onClick = {},
            enabled = false,
        )
    }
}
