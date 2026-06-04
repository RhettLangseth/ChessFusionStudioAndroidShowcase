package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.ui.navigation.ShowcaseDestination

internal val ShowcaseTopBarHeight = 40.dp
internal val ShowcaseTopBarButtonSize = 40.dp

@Composable
internal fun ShowcaseTopBar(
    destination: ShowcaseDestination,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    barHeight: Dp = ShowcaseTopBarHeight,
    centerContent: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = horizontalPadding)
    ) {
        if (destination != ShowcaseDestination.Start) {
            ShowcaseTopBarIconButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back",
                onClick = onBackClick,
                shape = RoundedCornerShape(6.dp),
                iconSize = 22.dp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Box(modifier = Modifier.align(Alignment.Center)) {
            centerContent()
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}

@Composable
internal fun ShowcaseTopBarIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(50),
    iconSize: Dp = 20.dp
) {
    Box(
        modifier = modifier
            .size(ShowcaseTopBarButtonSize)
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(iconSize)
        )
    }
}
