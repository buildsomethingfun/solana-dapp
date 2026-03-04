package dapp.buildsomething.feature.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.NavigationOption
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.ui.components.button.AppTextButton
import dapp.buildsomething.common.ui.components.button.PrimaryButton
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16Medium
import dapp.buildsomething.common.ui.style.Heading2
import dapp.buildsomething.feature.onboarding.presentation.OnboardingStore
import dapp.buildsomething.feature.onboarding.presentation.OnboardingStoreProvider
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEffect
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingUIEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navigator: Navigator,
    storeProvider: OnboardingStoreProvider,
) {
    val store = viewModel<OnboardingStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()

    fun handleEffect(effect: OnboardingEffect) {
        when (effect) {
            is OnboardingEffect.NavigateToAuth -> {
                navigator.open(AppDestination.Auth) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
            is OnboardingEffect.NavigateToHome -> {
                navigator.open(AppDestination.Home) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
            is OnboardingEffect.ShowError -> {
                navigator.open(AppDestination.ErrorToast(effect.message))
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach(::handleEffect)
            .launchIn(this)
    }

    OnboardingScreenContent(
        email = state.email,
        name = state.name,
        isLoading = state.isLoading,
        onEmailChanged = { store.accept(OnboardingUIEvent.EmailChanged(it)) },
        onNameChanged = { store.accept(OnboardingUIEvent.NameChanged(it)) },
        onUseDifferentWallet = { store.accept(OnboardingUIEvent.UseDifferentWalletClicked) },
        onSubmit = { store.accept(OnboardingUIEvent.SubmitClicked) },
    )
}

@Composable
private fun OnboardingScreenContent(
    email: String,
    name: String,
    isLoading: Boolean,
    onEmailChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onUseDifferentWallet: () -> Unit,
    onSubmit: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val isFirstPage = pagerState.currentPage == 0
    val isValid = if (isFirstPage) email.matches(EMAIL_REGEX) else name.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.ime),
    ) {
        Toolbar(
            showBack = !isFirstPage,
            onBackClick = {
                scope.launch { pagerState.animateScrollToPage(0) }
            },
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    question = "What's your\nemail?",
                    subtitle = "Your publisher email. It will be used to publish apps on the dApp Store.",
                    value = email,
                    onValueChange = onEmailChanged,
                    placeholder = "you@example.com",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        if (email.matches(EMAIL_REGEX)) {
                            scope.launch { pagerState.animateScrollToPage(1) }
                        }
                    },
                )
                1 -> OnboardingPage(
                    question = "What's your\nname?",
                    subtitle = "Your publisher name. It will be visible to everyone on the dApp Store.",
                    value = name,
                    onValueChange = onNameChanged,
                    placeholder = "Your name",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        if (name.isNotBlank()) focusManager.clearFocus()
                    },
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            if (isFirstPage) {
                AppTextButton(
                    text = "Use different wallet",
                    onClick = onUseDifferentWallet,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            PrimaryButton(
                text = if (isFirstPage) "Continue" else "Get started",
                onClick = {
                    if (isFirstPage) {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    } else {
                        onSubmit()
                    }
                },
                enabled = isValid,
                loading = isLoading,
            )
        }
    }
}

@Composable
private fun Toolbar(
    showBack: Boolean,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .statusBarsPadding()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        AnimatedVisibility(
            visible = showBack,
            enter = fadeIn() + slideInHorizontally { -it },
            exit = fadeOut() + slideOutHorizontally { -it },
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AppTheme.Colors.Text.Primary,
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    question: String,
    subtitle: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeAction: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = question,
            style = Heading2,
            color = AppTheme.Colors.Text.Primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = Body14Regular,
            color = AppTheme.Colors.Text.Secondary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = AppTheme.Colors.Background.Tertiary,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester),
            textStyle = Body16Medium.copy(color = AppTheme.Colors.Text.Primary),
            cursorBrush = SolidColor(AppTheme.Colors.Core.Accent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onNext = { onImeAction() },
                onDone = { onImeAction() },
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = Body16Medium,
                            color = AppTheme.Colors.Text.Tertiary,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
