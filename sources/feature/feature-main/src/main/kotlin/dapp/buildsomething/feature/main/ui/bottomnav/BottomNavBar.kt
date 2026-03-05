@file:OptIn(ExperimentalFoundationApi::class)

package dapp.buildsomething.feature.main.ui.bottomnav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dapp.buildsomething.common.ui.modifier.bouncingClickable
import dapp.buildsomething.common.ui.style.AppTheme

private val PillHeight = 48.dp
private val PillCornerRadius = 24.dp
private val PillHorizontalPadding = 24.dp
private val IconSize = 24.dp
private val PillWidth = IconSize + PillHorizontalPadding * 2

@Composable
internal fun BottomNavBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.Colors.Background.Surface),
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.Colors.Border.Primary,
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp),
        ) {
            val tabCount = Tab.entries.size
            val selectedIndex = Tab.entries.indexOf(selectedTab)
            val itemWidth = constraints.maxWidth.toFloat() / tabCount
            val density = LocalDensity.current

            val pillOffsetPx by androidx.compose.animation.core.animateFloatAsState(
                targetValue = itemWidth * selectedIndex + (itemWidth - with(density) { PillWidth.toPx() }) / 2f,
                animationSpec = spring(
                    dampingRatio = 0.75f,
                    stiffness = Spring.StiffnessLow,
                ),
                label = "pillOffset",
            )

            Box(
                modifier = Modifier
                    .offset { IntOffset(pillOffsetPx.toInt(), 0) }
                    .align(Alignment.CenterStart)
                    .width(PillWidth)
                    .height(PillHeight)
                    .background(
                        color = AppTheme.Colors.Text.Primary,
                        shape = RoundedCornerShape(PillCornerRadius),
                    ),
            )

            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Tab.entries.forEach { tab ->
                    BottomNavItem(
                        tab = tab,
                        isSelected = tab == selectedTab,
                        onClick = { onTabSelected(tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    tab: Tab,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (isSelected) {
            AppTheme.Colors.Text.PrimaryInversed
        } else {
            AppTheme.Colors.Text.Primary
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "iconTint",
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .height(PillHeight)
            .bouncingClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = tab.icon),
            contentDescription = tab.label,
            modifier = Modifier.size(IconSize),
            tint = tint,
        )
    }
}
