package dapp.buildsomething.feature.apps.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import timber.log.Timber
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import dapp.buildsomething.common.ui.AppPreview
import dapp.buildsomething.common.ui.components.button.AppTextButton
import dapp.buildsomething.common.ui.components.button.PillButton
import dapp.buildsomething.common.ui.components.button.PillButtonStyle
import dapp.buildsomething.common.ui.rememberAppShimmer
import dapp.buildsomething.common.ui.style.AppTheme
import dapp.buildsomething.common.ui.style.Body12Regular
import dapp.buildsomething.common.ui.style.Body14Medium
import dapp.buildsomething.common.ui.style.Body14Regular
import dapp.buildsomething.common.ui.style.Body16Regular
import dapp.buildsomething.common.ui.style.Heading2
import dapp.buildsomething.common.ui.style.Heading3
import dapp.buildsomething.common.ui.style.Heading4
import dapp.buildsomething.feature.apps.details.presentation.model.PublishingStep
import dapp.buildsomething.feature.apps.list.ui.AppImage
import dapp.buildsomething.repository.something.internal.api.model.AppStatus
import dapp.buildsomething.common.ui.R as CommonUiR

@Composable
internal fun AppDetailScreenContent(
    state: AppDetailScreenState,
    onBackClick: () -> Unit,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onPublishClick: () -> Unit,
    onPublishToStoreClick: () -> Unit,
    onRetryClick: () -> Unit,
    onMintClick: (String) -> Unit,
) {
    val contentScrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { contentScrollState.value > 0 } }
    val dividerAlpha by animateFloatAsState(if (isScrolled) 1f else 0f, label = "toolbar_divider")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = AppTheme.Colors.Text.Primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.Colors.Border.Primary.copy(alpha = dividerAlpha),
        )

        when (state) {
            AppDetailScreenState.Loading -> DetailLoading()
            AppDetailScreenState.Error -> DetailError(onRetryClick = onRetryClick)
            is AppDetailScreenState.Content -> DetailContent(
                scrollState = contentScrollState,
                app = state.app,
                isDeploying = state.isDeploying,
                publishingStep = state.publishingStep,
                onViewClick = onViewClick,
                onEditClick = onEditClick,
                onPublishClick = onPublishClick,
                onPublishToStoreClick = onPublishToStoreClick,
                onMintClick = onMintClick,
            )
        }
    }
}

@Composable
private fun DetailContent(
    scrollState: ScrollState,
    app: AppDetailUiModel,
    isDeploying: Boolean,
    publishingStep: PublishingStep?,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onPublishClick: () -> Unit,
    onPublishToStoreClick: () -> Unit,
    onMintClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.Top,
        ) {
            AppImage(imageUrl = app.iconUrl, modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    style = Heading3,
                    color = AppTheme.Colors.Text.Primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = app.androidPackage ?: "No package",
                    style = Body14Medium,
                    color = AppTheme.Colors.Text.Interactive,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        StatsRow(app = app)

        Spacer(modifier = Modifier.height(24.dp))

        ActionButtons(
            app = app,
            isDeploying = isDeploying,
            publishingStep = publishingStep,
            onViewClick = onViewClick,
            onEditClick = onEditClick,
            onPublishClick = onPublishClick,
            onPublishToStoreClick = onPublishToStoreClick,
        )

        if (publishingStep != null) {
            Spacer(modifier = Modifier.height(20.dp))
            PublishingProgress(
                currentStep = publishingStep,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (app.screenshots.isNotEmpty()) {
            ScreenshotsRow(screenshots = app.screenshots)
            Spacer(modifier = Modifier.height(24.dp))
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.Colors.Border.Primary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(title = "About this dApp")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = app.description ?: "No description provided",
            style = Body14Regular,
            color = AppTheme.Colors.Text.Secondary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TagRow(
            tags = buildList {
                add("Android")
                add("Solana Mobile")
                if (app.appMintAddress != null) add("On-chain")
            },
        )

        if (app.appMintAddress != null || app.publisherMintAddress != null) {
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = AppTheme.Colors.Border.Primary,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(title = "On-chain")

            Spacer(modifier = Modifier.height(12.dp))

            if (app.publisherMintAddress != null) {
                MintAddressRow(
                    label = "Publisher",
                    address = app.publisherMintAddress,
                    onClick = { onMintClick(app.publisherMintAddress) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (app.appMintAddress != null) {
                MintAddressRow(
                    label = "App",
                    address = app.appMintAddress,
                    onClick = { onMintClick(app.appMintAddress) },
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.Colors.Border.Primary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(title = "Data safety")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "The developer has provided this information about how this app collects, shares, and handles your data. " +
                "Data practices may vary based on your app usage, region, and age. " +
                "This app is subject to the buildsomething.fun privacy policy and terms of service.",
            style = Body14Regular,
            color = AppTheme.Colors.Text.Secondary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(
                    width = 1.dp,
                    color = AppTheme.Colors.Border.Primary,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DataSafetyItem(
                icon = CommonUiR.drawable.ic_lock,
                text = "Data is encrypted in transit",
            )

            DataSafetyItem(
                icon = CommonUiR.drawable.ic_solana,
                text = "Transactions signed via Solana Mobile Wallet Adapter",
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        DataSafetyItem(
            icon = CommonUiR.drawable.ic_chevron_right,
            text = "Privacy policy",
            interactive = true,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        DataSafetyItem(
            icon = CommonUiR.drawable.ic_chevron_right,
            text = "Terms of service",
            interactive = true,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActionButtons(
    app: AppDetailUiModel,
    isDeploying: Boolean,
    publishingStep: PublishingStep?,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onPublishClick: () -> Unit,
    onPublishToStoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when {
            app.appMintAddress != null -> {
                PillButton(
                    text = "Published",
                    onClick = {},
                    style = PillButtonStyle.Primary,
                    enabled = false,
                    modifier = Modifier.weight(1f),
                )
                if (app.deployedUrl != null) {
                    PillButton(
                        text = "Preview",
                        onClick = onViewClick,
                        style = PillButtonStyle.Outline,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            app.deployedUrl != null -> {
                PillButton(
                    text = "Publish to dApp Store",
                    onClick = onPublishToStoreClick,
                    style = PillButtonStyle.Primary,
                    enabled = publishingStep == null,
                    modifier = Modifier.weight(1f),
                )
                PillButton(
                    text = "Preview",
                    onClick = onViewClick,
                    style = PillButtonStyle.Outline,
                )
                PillButton(
                    text = "Edit",
                    onClick = onEditClick,
                    style = PillButtonStyle.Outline,
                )
            }
            else -> {
                PillButton(
                    text = "Deploy",
                    onClick = onPublishClick,
                    style = PillButtonStyle.Primary,
                    loading = isDeploying,
                    modifier = Modifier.weight(1f),
                )
                PillButton(
                    text = "Edit",
                    onClick = onEditClick,
                    style = PillButtonStyle.Outline,
                    enabled = !isDeploying,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun PublishingProgress(
    currentStep: PublishingStep,
    modifier: Modifier = Modifier,
) {
    val steps = PublishingStep.entries.filter { it != PublishingStep.Done }
    val currentOrdinal = currentStep.ordinal
    val isDone = currentStep == PublishingStep.Done

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = AppTheme.Colors.Border.Primary,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = if (isDone) "Published!" else "Publishing...",
            style = Heading4,
            color = AppTheme.Colors.Text.Primary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        steps.forEach { step ->
            val stepState = when {
                isDone || step.ordinal < currentOrdinal -> StepIndicatorState.Completed
                step.ordinal == currentOrdinal -> StepIndicatorState.Current
                else -> StepIndicatorState.Pending
            }
            StepIndicatorRow(
                label = step.displayName,
                state = stepState,
            )
        }
    }
}

private val PublishingStep.displayName: String
    get() = when (this) {
        PublishingStep.CreatingRelease -> "Creating release"
        PublishingStep.RequestingPayment -> "Requesting build payment"
        PublishingStep.SigningPayment -> "Signing SOL payment"
        PublishingStep.ConfirmingBuild -> "Confirming build"
        PublishingStep.Building -> "Building APK"
        PublishingStep.GeneratingLegal -> "Generating legal docs"
        PublishingStep.GeneratingScreenshots -> "Generating screenshots"
        PublishingStep.GeneratingIcon -> "Generating app icon"
        PublishingStep.CreatingPublisherNft -> "Creating Publisher NFT"
        PublishingStep.SigningPublisherNft -> "Signing Publisher NFT"
        PublishingStep.CreatingAppNft -> "Creating App NFT"
        PublishingStep.SigningAppNft -> "Signing App NFT"
        PublishingStep.CreatingReleaseNft -> "Creating Release NFT"
        PublishingStep.SigningReleaseNft -> "Signing Release NFT"
        PublishingStep.PreparingAttestation -> "Preparing attestation"
        PublishingStep.SigningAttestation -> "Signing attestation"
        PublishingStep.Publishing -> "Submitting to dApp Store"
        PublishingStep.Done -> "Done"
    }

private enum class StepIndicatorState { Completed, Current, Pending }

@Composable
private fun StepIndicatorRow(
    label: String,
    state: StepIndicatorState,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        when (state) {
            StepIndicatorState.Completed -> {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            StepIndicatorState.Current -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = AppTheme.Colors.Core.Accent,
                )
            }
            StepIndicatorState.Pending -> {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(1.dp, AppTheme.Colors.Text.Tertiary, CircleShape),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            style = if (state == StepIndicatorState.Current) Body14Medium else Body14Regular,
            color = when (state) {
                StepIndicatorState.Completed -> AppTheme.Colors.Text.Secondary
                StepIndicatorState.Current -> AppTheme.Colors.Text.Primary
                StepIndicatorState.Pending -> AppTheme.Colors.Text.Tertiary
            },
        )
    }
}

@Composable
private fun StatsRow(app: AppDetailUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatItem(
            label = "Status",
            value = app.status.name,
            modifier = Modifier.weight(1f),
        )
        StatDivider()
        StatItem(
            label = "Updated",
            value = app.updatedAt,
            modifier = Modifier.weight(1f),
        )
        StatDivider()
        StatItem(
            label = "Created",
            value = app.createdAt,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = Body14Medium,
            color = AppTheme.Colors.Text.Primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = Body12Regular,
            color = AppTheme.Colors.Text.Tertiary,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StatDivider() {
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 8.dp),
        thickness = 1.dp,
        color = AppTheme.Colors.Border.Primary,
    )
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = Heading4,
        color = AppTheme.Colors.Text.Primary,
        modifier = Modifier.padding(horizontal = 24.dp),
    )
}

@Composable
private fun TagRow(tags: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tags.forEach { tag ->
            Text(
                text = tag,
                style = Body12Regular,
                color = AppTheme.Colors.Text.Secondary,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = AppTheme.Colors.Border.Primary,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun MintAddressRow(
    label: String,
    address: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = Body14Regular,
            color = AppTheme.Colors.Text.Secondary,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = address.take(4) + "..." + address.takeLast(4),
            style = Body14Medium,
            color = AppTheme.Colors.Text.Interactive,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = CommonUiR.drawable.ic_external_link),
            contentDescription = null,
            tint = AppTheme.Colors.Text.Interactive,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun DataSafetyItem(icon: Int, text: String, interactive: Boolean = false, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (interactive) AppTheme.Colors.Text.Interactive else AppTheme.Colors.Text.Tertiary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = if (interactive) Body14Medium else Body14Regular,
            color = if (interactive) AppTheme.Colors.Text.Interactive else AppTheme.Colors.Text.Secondary,
        )
    }
}

@Composable
private fun ScreenshotsRow(screenshots: List<String>) {
    Column {
        SectionHeader(title = "Screenshots")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(screenshots) { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillParentMaxWidth(0.6f)
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }
    }
}

@Composable
private fun DetailLoading() {
    val shimmer = rememberAppShimmer()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .shimmer(shimmer)
            .verticalScroll(rememberScrollState()),
    ) {
        // Icon + name + package (matches DetailContent header)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppTheme.Colors.Background.Tertiary),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 180.dp, height = 22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(width = 130.dp, height = 14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            repeat(3) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AppTheme.Colors.Background.Tertiary),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AppTheme.Colors.Background.Tertiary),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(48.dp))
                    .background(AppTheme.Colors.Background.Tertiary),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(48.dp))
                    .background(AppTheme.Colors.Background.Tertiary),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Screenshots
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false,
        ) {
            items(2) {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth(0.6f)
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divider
        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.Colors.Background.Tertiary,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // "About this dApp" header
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .size(width = 140.dp, height = 20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(AppTheme.Colors.Background.Tertiary),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description lines
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppTheme.Colors.Background.Tertiary),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(0.7f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppTheme.Colors.Background.Tertiary),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tags
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 28.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppTheme.Colors.Background.Tertiary),
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DetailError(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Oops...",
            style = Heading2,
            color = AppTheme.Colors.Text.Primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We couldn't load this app",
            style = Body16Regular,
            color = AppTheme.Colors.Text.Secondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        AppTextButton(
            text = "Try again",
            onClick = onRetryClick,
            iconStart = CommonUiR.drawable.ic_retry,
        )
    }
}

@AppPreview
@Composable
private fun DeployedPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        AppDetailScreenContent(
            state = AppDetailScreenState.Content(
                app = AppDetailUiModel(
                    id = "1",
                    name = "My Todo App",
                    description = "A beautiful todo app built with React and Cloudflare Workers. Manage your tasks with a clean UI and fast backend.",
                    status = AppStatus.Deployed,
                    deployedUrl = "https://app-123.workers.dev",
                    screenshots = listOf("https://example.com/screenshot1.png", "https://example.com/screenshot2.png"),
                    androidPackage = "fun.buildsomething.mytodoapp",
                    appMintAddress = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgAsU",
                    publisherMintAddress = "3UtSqTwb7THrcRPcwKHMU6gUG2GRgUw5msXuHzQtx6uF",
                    iconUrl = null,
                    licenseUrl = "https://opensource.org/licenses/MIT",
                    privacyPolicyUrl = "https://example.com/privacy",
                    copyrightUrl = null,
                    createdAt = "Feb 23, 2026",
                    updatedAt = "Feb 24, 2026",
                ),
            ),
            onBackClick = {},
            onViewClick = {},
            onEditClick = {},
            onPublishClick = {},
            onPublishToStoreClick = {},
            onRetryClick = {},
            onMintClick = {},
        )
    }
}

@AppPreview
@Composable
private fun DraftPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        AppDetailScreenContent(
            state = AppDetailScreenState.Content(
                app = AppDetailUiModel(
                    id = "2",
                    name = "Untitled App",
                    description = null,
                    status = AppStatus.Draft,
                    deployedUrl = null,
                    screenshots = emptyList(),
                    androidPackage = null,
                    appMintAddress = null,
                    publisherMintAddress = null,
                    iconUrl = null,
                    licenseUrl = null,
                    privacyPolicyUrl = null,
                    copyrightUrl = null,
                    createdAt = "Feb 24, 2026",
                    updatedAt = "Feb 24, 2026",
                ),
            ),
            onBackClick = {},
            onViewClick = {},
            onEditClick = {},
            onPublishClick = {},
            onPublishToStoreClick = {},
            onRetryClick = {},
            onMintClick = {},
        )
    }
}

@AppPreview
@Composable
private fun PublishingPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        AppDetailScreenContent(
            state = AppDetailScreenState.Content(
                app = AppDetailUiModel(
                    id = "3",
                    name = "My Todo App",
                    description = "A beautiful todo app",
                    status = AppStatus.Deployed,
                    deployedUrl = "https://app-123.workers.dev",
                    screenshots = emptyList(),
                    androidPackage = "fun.buildsomething.mytodoapp",
                    appMintAddress = null,
                    publisherMintAddress = null,
                    iconUrl = null,
                    licenseUrl = null,
                    privacyPolicyUrl = null,
                    copyrightUrl = null,
                    createdAt = "Feb 24, 2026",
                    updatedAt = "Feb 24, 2026",
                ),
                publishingStep = PublishingStep.Building,
            ),
            onBackClick = {},
            onViewClick = {},
            onEditClick = {},
            onPublishClick = {},
            onPublishToStoreClick = {},
            onRetryClick = {},
            onMintClick = {},
        )
    }
}

@AppPreview
@Composable
private fun LoadingPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Colors.Background.Primary),
    ) {
        AppDetailScreenContent(
            state = AppDetailScreenState.Loading,
            onBackClick = {},
            onViewClick = {},
            onEditClick = {},
            onPublishClick = {},
            onPublishToStoreClick = {},
            onRetryClick = {},
            onMintClick = {},
        )
    }
}
