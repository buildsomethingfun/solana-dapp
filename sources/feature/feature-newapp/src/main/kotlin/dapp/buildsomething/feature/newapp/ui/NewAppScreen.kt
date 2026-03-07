@file:OptIn(ExperimentalFoundationApi::class)

package dapp.buildsomething.feature.newapp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.Navigator
import androidx.compose.foundation.shape.RoundedCornerShape
import com.valentinilk.shimmer.shimmer
import dapp.buildsomething.common.ui.R
import dapp.buildsomething.common.ui.modifier.bouncingClickable
import dapp.buildsomething.common.ui.rememberAppShimmer
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.util.openUrl
import dapp.buildsomething.common.ui.style.Body20SemiBold
import dapp.buildsomething.feature.newapp.presentation.NewAppStore
import dapp.buildsomething.feature.newapp.presentation.NewAppStoreProvider
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEffect
import dapp.buildsomething.feature.newapp.presentation.model.NewAppUIEvent
import dapp.buildsomething.feature.newapp.presentation.model.NewAppUIEvent.LoadChatHistory
import dapp.buildsomething.feature.newapp.ui.model.NewAppUiState
import dapp.buildsomething.feature.newapp.ui.components.input.ChatInput
import dapp.buildsomething.feature.newapp.ui.components.message.AssistantMessage
import dapp.buildsomething.feature.newapp.ui.components.message.UserMessageBubble
import dapp.buildsomething.feature.newapp.ui.components.message.StreamingIndicator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NewAppScreen(
    navigator: Navigator,
    storeProvider: NewAppStoreProvider,
    appId: String? = null,
    appName: String? = null,
) {
    val store = viewModel<NewAppStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()

    LaunchedEffect(appId) {
        if (appId != null) {
            store.accept(LoadChatHistory(appId = appId, appName = appName.orEmpty()))
        }
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach { effect ->
                when (effect) {
                    is NewAppEffect.ShowError -> {
                        navigator.open(AppDestination.ErrorToast(effect.message))
                    }
                    is NewAppEffect.NavigateToAppDetail -> {
                        navigator.open(AppDestination.AppDetail(effect.appId))
                    }
                }
            }
            .launchIn(this)
    }

    val context = LocalContext.current

    ChatScreenContent(
        state = state,
        isFullscreen = appId != null,
        onBackClick = { navigator.back() },
        onInputChanged = { store.accept(NewAppUIEvent.InputChanged(it)) },
        onSendClick = { store.accept(NewAppUIEvent.SendMessage) },
        onPublishClick = { store.accept(NewAppUIEvent.PublishClicked) },
        onPreviewClick = { url -> context.openUrl(url) },
        onNewChatClick = { store.accept(NewAppUIEvent.NewChat) },
    )
}

@Composable
private fun ChatScreenContent(
    state: NewAppUiState,
    isFullscreen: Boolean,
    onBackClick: () -> Unit,
    onInputChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onPublishClick: () -> Unit,
    onPreviewClick: (url: String) -> Unit,
    onNewChatClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }
    val dividerAlpha by animateFloatAsState(if (isScrolled) 1f else 0f, label = "toolbar_divider")

    LaunchedEffect(state.messages.size, state.messages.lastOrNull()?.text) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary)
            .statusBarsPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Toolbar(
                title = state.title,
                showBack = state.isFullscreen,
                showNewChat = !state.isFullscreen,
                onBackClick = onBackClick,
                onNewChatClick = onNewChatClick,
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = AppTheme.Colors.Border.Primary.copy(alpha = dividerAlpha),
            )

            AnimatedContent(
                targetState = state.isLoadingHistory,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.weight(1f),
                label = "chat_content",
            ) { isLoading ->
                if (isLoading) {
                    ChatHistoryShimmer(modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 72.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.messages) { message ->
                            if (message.isUser) {
                                UserMessageBubble(
                                    text = message.text,
                                    modifier = Modifier.animateItem(),
                                )
                            } else {
                                AssistantMessage(
                                    text = message.text,
                                    tools = message.tools,
                                    isStreaming = state.isStreaming,
                                    onPublishClick = onPublishClick,
                                    onPreviewClick = onPreviewClick,
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }

                        if (state.isStreaming && (state.messages.isEmpty() || state.messages.last().isUser)) {
                            item { StreamingIndicator(modifier = Modifier.animateItem()) }
                        }

                        item { Spacer(Modifier.height(72.dp)) }
                    }
                }
            }
        }

        ChatInput(
            text = state.inputText,
            isStreaming = state.isStreaming,
            onTextChanged = onInputChanged,
            onSendClick = onSendClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(AppTheme.Colors.Background.Primary)
                .then(
                    if (isFullscreen) {
                        Modifier
                            .navigationBarsPadding()
                            .imePadding()
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Composable
private fun Toolbar(
    title: String,
    showBack: Boolean,
    showNewChat: Boolean,
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AppTheme.Colors.Text.Primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = Body20SemiBold,
            color = AppTheme.Colors.Text.Primary,
            modifier = Modifier.weight(1f),
        )
        if (showNewChat) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New chat",
                tint = AppTheme.Colors.Text.Primary,
                modifier = Modifier
                    .size(24.dp)
                    .bouncingClickable(onClick = onNewChatClick),
            )
        }
    }
}

@Composable
private fun ChatHistoryShimmer(modifier: Modifier = Modifier) {
    val shimmer = rememberAppShimmer()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(shimmer)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(8.dp))
        MessageBubbleSkeleton(widthFraction = 0.6f, alignEnd = true)
        MessageBubbleSkeleton(widthFraction = 0.85f, alignEnd = false, lines = 3)
        MessageBubbleSkeleton(widthFraction = 0.5f, alignEnd = true)
        MessageBubbleSkeleton(widthFraction = 0.75f, alignEnd = false, lines = 2)
    }
}

@Composable
private fun MessageBubbleSkeleton(
    widthFraction: Float,
    alignEnd: Boolean,
    lines: Int = 1,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (alignEnd) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(lines) { i ->
                val fraction = if (i == lines - 1 && lines > 1) widthFraction * 0.7f else widthFraction
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }
    }
}
