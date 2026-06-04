package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.ui.navigation.ShowcaseDestination

@Composable
internal fun ShowcasePageScaffold(
    destination: ShowcaseDestination,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = destination.showsTopBar,
    topBarHeight: Dp = ShowcaseTopBarHeight,
    topBarBottomSpacing: Dp = AppSpacing.Page,
    topBarCenterContent: @Composable () -> Unit = {},
    topBarActions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val containerSize = LocalWindowInfo.current.containerSize
    val isLandscape = containerSize.width > containerSize.height
    val outerHorizontalPadding = if (isLandscape) 0.dp else AppSpacing.Page
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
    val safeDrawing = WindowInsets.safeDrawing

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showTopBar) {
                Box(
                    modifier = Modifier.windowInsetsPadding(
                        safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                    )
                ) {
                    ShowcaseTopBar(
                        destination = destination,
                        onBackClick = onBackClick,
                        horizontalPadding = outerHorizontalPadding,
                        barHeight = topBarHeight,
                        centerContent = topBarCenterContent,
                        actions = topBarActions
                    )
                }
            }
        },
        content = { innerPadding ->
            val safeStart = with(density) {
                safeDrawing.getLeft(density, layoutDirection).toDp()
            }
            val safeEnd = with(density) {
                safeDrawing.getRight(density, layoutDirection).toDp()
            }
            val safeTop = with(density) {
                safeDrawing.getTop(density).toDp()
            }
            val safeBottom = with(density) {
                safeDrawing.getBottom(density).toDp()
            }
            val mergedPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(layoutDirection) + safeStart + outerHorizontalPadding,
                top = innerPadding.calculateTopPadding() + if (showTopBar) topBarBottomSpacing else safeTop,
                end = innerPadding.calculateEndPadding(layoutDirection) + safeEnd + outerHorizontalPadding,
                bottom = innerPadding.calculateBottomPadding() + safeBottom
            )
            content(mergedPadding)
        }
    )
}
