package dapp.buildsomething.feature.newapp.ui.components.message

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.R
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body12Medium
import dapp.buildsomething.common.ui.style.Body12Regular
import dapp.buildsomething.common.ui.style.Body14Medium
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16Regular
import dapp.buildsomething.common.ui.components.button.PillButton
import dapp.buildsomething.common.ui.components.button.PillButtonStyle
import dapp.buildsomething.feature.newapp.ui.model.ToolCallUi
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
internal fun AssistantMessage(
    text: String,
    tools: List<ToolCallUi>,
    isStreaming: Boolean,
    onPublishClick: () -> Unit = {},
    onPreviewClick: (url: String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (isStreaming && text.isEmpty()) {
            StreamingIndicator()
            if (tools.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
            }
        }

        if (tools.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                tools.forEach { tool ->
                    when (tool) {
                        is ToolCallUi.CreateFile -> CreateFileToolCall(tool)
                        is ToolCallUi.ReadFile -> ReadFileToolCall(tool)
                        is ToolCallUi.CreateSchema -> CreateSchemaToolCall(tool)
                        is ToolCallUi.ListFiles -> ListFilesToolCall(tool)
                        is ToolCallUi.DeployPreview -> DeployPreviewToolCall(tool, onPublishClick, onPreviewClick)
                        is ToolCallUi.Generic -> GenericToolCall(tool)
                    }
                }
            }
            if (text.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
            }
        }

        if (text.isNotEmpty()) {
            MarkdownText(
                markdown = text,
                style = Body16Regular.copy(
                    color = AppTheme.Colors.Text.Primary
                ),
            )
        }
    }
}

// region Shimmer

@Composable
internal fun rememberShimmerBrush(): Brush {
    val color = AppTheme.Colors.Text.Secondary
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
        ),
        label = "shimmerOffset",
    )
    return Brush.linearGradient(
        colors = listOf(
            color.copy(alpha = 0.3f),
            color.copy(alpha = 0.9f),
            color.copy(alpha = 0.3f),
        ),
        start = Offset(offset - 150f, 0f),
        end = Offset(offset, 0f),
    )
}

// endregion

// region Shared card components

@Composable
private fun ToolCallCard(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(AppTheme.Colors.Background.Tertiary)
            .animateContentSize()
            .padding(10.dp),
    ) {
        content()
    }
}

@Composable
private fun ToolCallHeader(
    title: String,
    isCompleted: Boolean,
    hasError: Boolean,
    badge: @Composable (() -> Unit)? = null,
    expandable: Boolean = false,
    expanded: Boolean = false,
    onExpandToggle: (() -> Unit)? = null,
) {
    val shimmerBrush = rememberShimmerBrush()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        // Status icon
        if (isCompleted) {
            val iconColor = if (hasError) {
                AppTheme.Colors.Extension.Red
            } else {
                AppTheme.Colors.Extension.Green
            }
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(6.dp))
        }

        // Title
        if (isCompleted) {
            Text(
                text = title,
                style = Body14Medium,
                color = AppTheme.Colors.Text.Secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
        } else {
            Text(
                text = title,
                style = Body14Medium.copy(brush = shimmerBrush),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
        }

        // Badge
        if (badge != null) {
            Spacer(Modifier.width(6.dp))
            badge()
        }

        // Expand chevron
        if (expandable) {
            Spacer(Modifier.width(6.dp))
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 90f else 0f,
                label = "chevron",
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = AppTheme.Colors.Text.Tertiary,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotation)
                    .clickable { onExpandToggle?.invoke() },
            )
        }
    }
}

@Composable
private fun FileTypeBadge(extension: String) {
    if (extension.isBlank()) return
    val (bgColor, textColor) = badgeColors(extension)
    Text(
        text = extension.uppercase(),
        style = Body12Medium,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 5.dp, vertical = 1.dp),
    )
}

private fun badgeColors(ext: String): Pair<Color, Color> = when (ext.lowercase()) {
    "html", "htm" -> Color(0x33FF6347) to Color(0xFFFF6347)
    "js", "jsx", "ts", "tsx" -> Color(0x33F7DF1E) to Color(0xFFD4B800)
    "css", "scss" -> Color(0x332196F3) to Color(0xFF2196F3)
    "sql" -> Color(0x33FFB300) to Color(0xFFFFB300)
    "json" -> Color(0x334CAF50) to Color(0xFF4CAF50)
    "kt", "kts" -> Color(0x337F52FF) to Color(0xFF7F52FF)
    "py" -> Color(0x333776AB) to Color(0xFF3776AB)
    "md" -> Color(0x33808080) to Color(0xFF808080)
    "xml" -> Color(0x33FF9800) to Color(0xFFFF9800)
    "svg" -> Color(0x33FFB300) to Color(0xFFFFB300)
    else -> Color(0x33808080) to Color(0xFF808080)
}

@Composable
private fun CodePreviewBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(AppTheme.Colors.Background.Surface)
            .padding(8.dp),
    ) {
        Text(
            text = code,
            style = Body12Regular.copy(fontFamily = FontFamily.Monospace),
            color = AppTheme.Colors.Text.Secondary,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// endregion

// region Per-tool composables

@Composable
private fun CreateFileToolCall(tool: ToolCallUi.CreateFile) {
    var expanded by remember { mutableStateOf(false) }
    val hasPreview = tool.contentPreview != null && tool.isCompleted

    ToolCallCard {
        Column {
            ToolCallHeader(
                title = tool.filePath,
                isCompleted = tool.isCompleted,
                hasError = tool.hasError,
                badge = { FileTypeBadge(tool.extension) },
                expandable = hasPreview,
                expanded = expanded,
                onExpandToggle = { expanded = !expanded },
            )
            if (hasPreview && expanded && tool.contentPreview != null) {
                Spacer(Modifier.height(8.dp))
                CodePreviewBlock(tool.contentPreview)
            }
        }
    }
}

@Composable
private fun ReadFileToolCall(tool: ToolCallUi.ReadFile) {
    ToolCallCard {
        ToolCallHeader(
            title = tool.filePath,
            isCompleted = tool.isCompleted,
            hasError = tool.hasError,
            badge = { FileTypeBadge(tool.extension) },
        )
    }
}

@Composable
private fun CreateSchemaToolCall(tool: ToolCallUi.CreateSchema) {
    var expanded by remember { mutableStateOf(false) }
    val hasPreview = tool.sqlPreview != null && tool.isCompleted

    ToolCallCard {
        Column {
            ToolCallHeader(
                title = "Define schema",
                isCompleted = tool.isCompleted,
                hasError = tool.hasError,
                badge = { FileTypeBadge("sql") },
                expandable = hasPreview,
                expanded = expanded,
                onExpandToggle = { expanded = !expanded },
            )
            if (hasPreview && expanded && tool.sqlPreview != null) {
                Spacer(Modifier.height(8.dp))
                CodePreviewBlock(tool.sqlPreview)
            }
        }
    }
}

@Composable
private fun ListFilesToolCall(tool: ToolCallUi.ListFiles) {
    ToolCallCard {
        Column {
            ToolCallHeader(
                title = "List files",
                isCompleted = tool.isCompleted,
                hasError = tool.hasError,
            )
            if (tool.isCompleted && tool.files.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = tool.files.joinToString(", "),
                    style = Body12Regular,
                    color = AppTheme.Colors.Text.Tertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                )
            }
        }
    }
}

@Composable
private fun DeployPreviewToolCall(
    tool: ToolCallUi.DeployPreview,
    onPublishClick: () -> Unit,
    onPreviewClick: (url: String) -> Unit,
) {
    if (tool.isCompleted && !tool.hasError && tool.url != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppTheme.Colors.Background.Tertiary)
                .animateContentSize()
                .padding(12.dp),
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = AppTheme.Colors.Extension.Green,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Deployed",
                        style = Body14Medium,
                        color = AppTheme.Colors.Text.Secondary,
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PillButton(
                        text = "Preview",
                        onClick = { onPreviewClick(tool.url) },
                        style = PillButtonStyle.Outline,
                        iconStart = R.drawable.ic_external_link,
                        modifier = Modifier.weight(1f),
                    )
                    PillButton(
                        text = "Publish",
                        onClick = onPublishClick,
                        style = PillButtonStyle.Primary,
                        iconEnd = R.drawable.ic_arrow_up,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    } else {
        ToolCallCard {
            ToolCallHeader(
                title = "Deploy preview",
                isCompleted = tool.isCompleted,
                hasError = tool.hasError,
            )
        }
    }
}

@Composable
private fun GenericToolCall(tool: ToolCallUi.Generic) {
    ToolCallCard {
        ToolCallHeader(
            title = tool.label,
            isCompleted = tool.isCompleted,
            hasError = tool.hasError,
        )
    }
}

// endregion
