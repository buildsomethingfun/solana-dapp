@file:OptIn(ExperimentalFoundationApi::class)

package dapp.buildsomething.feature.newapp.ui.components.input

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.R
import dapp.buildsomething.common.ui.modifier.bouncingClickable
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body16Regular

@Composable
internal fun ChatInput(
    text: String,
    isStreaming: Boolean,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = { Unit },
    onSendClick: () -> Unit = { Unit },
) {
    val canSend = !isStreaming && text.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .background(
                color = AppTheme.Colors.Background.Tertiary,
                shape = RoundedCornerShape(32.dp),
            ),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .verticalScroll(rememberScrollState()),
            value = text,
            onValueChange = onTextChanged,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 48.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Build something",
                            style = Body16Regular,
                            color = AppTheme.Colors.Text.Secondary,
                        )
                    }

                    innerTextField()
                }
            },
            cursorBrush = SolidColor(AppTheme.Colors.Core.Accent),
            textStyle = Body16Regular.copy(color = AppTheme.Colors.Text.Primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = { if (canSend) onSendClick() },
            ),
            maxLines = 4,
            minLines = 1,
            enabled = !isStreaming,
        )

        Box(
            modifier = Modifier
                .padding(all = 8.dp)
                .size(32.dp)
                .alpha(if (canSend) 1f else .4f)
                .clip(CircleShape)
                .background(AppTheme.Colors.Text.Primary, CircleShape)
                .bouncingClickable(enabled = canSend, onClick = onSendClick)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up),
                tint = AppTheme.Colors.Text.PrimaryInversed,
                modifier = Modifier.size(24.dp),
                contentDescription = "Send",
            )
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary)
    ) {
        ChatInput(
            text = "",
            isStreaming = false,
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.Colors.Background.Surface)
                .align(Alignment.BottomCenter)
        )
    }
}