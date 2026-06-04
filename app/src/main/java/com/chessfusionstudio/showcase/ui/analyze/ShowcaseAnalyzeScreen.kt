package com.chessfusionstudio.showcase.ui.analyze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chessfusionstudio.showcase.ui.components.AppSpacing
import com.chessfusionstudio.showcase.ui.components.ShowcaseChessBoard
import com.chessfusionstudio.showcase.ui.components.ShowcasePageScaffold
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBar
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBarHeight
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBarIconButton
import com.chessfusionstudio.showcase.ui.navigation.ShowcaseDestination
import com.chessfusionstudio.showcase.ui.showcase.ThemeStudioViewModel

@Composable
internal fun ShowcaseAnalyzeRoute(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThemeStudioViewModel = viewModel(factory = ThemeStudioViewModel.factory(LocalContext.current.applicationContext))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShowcasePageScaffold(
        destination = ShowcaseDestination.Analyze,
        onBackClick = onBackClick,
        showTopBar = false,
        modifier = modifier
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val boardWidth = if (maxWidth > maxHeight) {
                minOf(maxWidth * 0.46f, maxHeight - ShowcaseTopBarHeight - (AppSpacing.Section * 3f))
                    .coerceAtLeast(180.dp)
            } else {
                maxWidth
            }

            Column(modifier = Modifier.fillMaxSize()) {
                ShowcaseTopBar(
                    destination = ShowcaseDestination.Analyze,
                    onBackClick = onBackClick,
                    actions = {
                        ShowcaseTopBarIconButton(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Open settings",
                            onClick = onSettingsClick
                        )
                    }
                )
                Spacer(modifier = Modifier.height(AppSpacing.Section))
                ShowcaseChessBoard(
                    previewState = uiState.previewState,
                    modifier = Modifier
                        .width(boardWidth)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(AppSpacing.Section))
                AnalyzeExplanationSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AnalyzeExplanationSection(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Analyze Page",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "In the full Android app, this page is the main workspace for reviewing a chess game. The board, move list, saved game data, and engine output all stay connected so a player can study decisions and preserve the game afterward."
            )
            Text(
                text = "The production version supports PGN tabs, move navigation, legal move interaction, game-data editing, save/open flows, board flipping, and engine analysis. This showcase keeps the board renderer and state-driven UI visible while highlighting the larger workflow."
            )
            Text(
                text = "The board above is rendered from the same simplified preview state used by the settings page. Changing the showcase settings persists values locally and immediately changes the rendered board."
            )
        }
    }
}
