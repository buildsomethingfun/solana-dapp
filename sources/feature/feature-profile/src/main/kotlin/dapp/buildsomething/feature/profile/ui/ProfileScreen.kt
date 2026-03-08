@file:OptIn(ExperimentalFoundationApi::class)

package dapp.buildsomething.feature.profile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dapp.buildsomething.common.navigation.AppDestination
import dapp.buildsomething.common.navigation.core.NavigationOption
import dapp.buildsomething.common.navigation.core.Navigator
import dapp.buildsomething.common.ui.R
import dapp.buildsomething.common.ui.modifier.bouncingClickable
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body14Medium
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16Medium
import dapp.buildsomething.common.ui.style.Body16SemiBold
import dapp.buildsomething.common.ui.style.Heading3
import dapp.buildsomething.common.util.openUrl
import dapp.buildsomething.feature.profile.presentation.ProfileStore
import dapp.buildsomething.feature.profile.presentation.ProfileStoreProvider
import dapp.buildsomething.feature.profile.presentation.model.ProfileEffect
import dapp.buildsomething.feature.profile.presentation.model.ProfileUIEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ProfileScreen(
    navigator: Navigator,
    storeProvider: ProfileStoreProvider,
) {
    val store = viewModel<ProfileStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()
    val context = LocalContext.current

    fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.NavigateToAuth -> {
                navigator.open(AppDestination.Auth) {
                    +NavigationOption.ClearStack
                    +NavigationOption.SingleTop
                }
            }
            is ProfileEffect.ShowError -> {
                navigator.open(AppDestination.ErrorToast(effect.message))
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects
            .onEach(::handleEffect)
            .launchIn(this)
    }

    ProfileScreenContent(
        displayName = state.displayName,
        email = state.email,
        walletAddress = state.walletAddress,
        avatarUrl = state.avatarUrl,
        isLoading = state.isLoading,
        onSignOut = { store.accept(ProfileUIEvent.SignOutClicked) },
        onOpenUrl = { context.openUrl(it) },
    )
}

@Composable
private fun ProfileScreenContent(
    displayName: String,
    email: String,
    walletAddress: String,
    avatarUrl: String?,
    isLoading: Boolean,
    onSignOut: () -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Avatar
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AppTheme.Colors.Background.Tertiary, CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(AppTheme.Colors.Background.Tertiary, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = AppTheme.Colors.Text.Tertiary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (displayName.isNotBlank()) {
                Text(
                    text = displayName,
                    style = Heading3,
                    color = AppTheme.Colors.Text.Primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (walletAddress.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.bouncingClickable {
                        onOpenUrl("https://explorer.solana.com/address/$walletAddress")
                    },
                ) {
                    Text(
                        text = walletAddress.take(4) + "..." + walletAddress.takeLast(4),
                        style = Body14Regular,
                        color = AppTheme.Colors.Core.Accent,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_external_link),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = AppTheme.Colors.Core.Accent,
                    )
                }
            }
        }

        // KYC Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    color = AppTheme.Colors.Core.Accent.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = AppTheme.Colors.Core.Accent.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .bouncingClickable { onOpenUrl("https://publish.solanamobile.com/") }
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Verify your identity",
                        style = Body16SemiBold,
                        color = AppTheme.Colors.Core.Accent,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Complete KYC verification on Solana Mobile to publish on the dApp Store.",
                        style = Body14Regular,
                        color = AppTheme.Colors.Text.Secondary,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.Colors.Core.Accent,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Section
        ProfileSection(title = "Account") {
            ProfileInfoRow(label = "Name", value = displayName)
            SectionDivider()
            ProfileInfoRow(label = "Email", value = email)
            SectionDivider()
            ProfileInfoRow(
                label = "Wallet",
                value = if (walletAddress.isNotBlank()) {
                    walletAddress.take(4) + "..." + walletAddress.takeLast(4)
                } else "",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Links Section
        ProfileSection(title = "Legal") {
            ProfileLinkRow(text = "Terms of Service") {
                onOpenUrl("https://buildsomething.fun/terms")
            }
            SectionDivider()
            ProfileLinkRow(text = "Privacy Policy") {
                onOpenUrl("https://buildsomething.fun/privacy")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign out
        ProfileSection {
            ProfileLinkRow(
                text = "Sign out",
                tint = AppTheme.Colors.Extension.Red,
                showChevron = false,
                onClick = onSignOut,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileSection(
    title: String? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        if (title != null) {
            Text(
                text = title,
                style = Body14Medium,
                color = AppTheme.Colors.Text.Secondary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppTheme.Colors.Background.Surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = AppTheme.Colors.Border.Primary,
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = Body16Medium,
            color = AppTheme.Colors.Text.Primary,
        )
        Text(
            text = value,
            style = Body14Regular,
            color = AppTheme.Colors.Text.Secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}

@Composable
private fun ProfileLinkRow(
    text: String,
    tint: Color = AppTheme.Colors.Text.Primary,
    showChevron: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bouncingClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = Body16Medium,
            color = tint,
        )
        if (showChevron) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AppTheme.Colors.Text.Tertiary,
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = AppTheme.Colors.Border.Primary,
    )
}
