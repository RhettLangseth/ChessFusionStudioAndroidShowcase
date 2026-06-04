package com.chessfusionstudio.showcase.ui.analyze

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardOrientation
import com.chessfusionstudio.showcase.ui.components.AppSpacing
import com.chessfusionstudio.showcase.ui.components.DropdownSetting
import com.chessfusionstudio.showcase.ui.components.ShowcaseChessBoard
import com.chessfusionstudio.showcase.ui.components.ShowcasePageScaffold
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBar
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBarHeight
import com.chessfusionstudio.showcase.ui.components.ShowcaseTopBarIconButton
import com.chessfusionstudio.showcase.ui.navigation.ShowcaseDestination
import com.chessfusionstudio.showcase.ui.showcase.PositionOption
import com.chessfusionstudio.showcase.ui.showcase.ThemeStudioViewModel

@Composable
internal fun ShowcaseAnalyzeRoute(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThemeStudioViewModel = viewModel(factory = ThemeStudioViewModel.factory(LocalContext.current.applicationContext))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isBoardFlipped by rememberSaveable { mutableStateOf(false) }
    var isExplanationExpanded by rememberSaveable { mutableStateOf(false) }
    val orientation = if (isBoardFlipped) {
        ShowcaseBoardOrientation.BlackAtBottom
    } else {
        ShowcaseBoardOrientation.WhiteAtBottom
    }
    val previewState = uiState.previewState.copy(orientation = orientation)
    val positionIndex = uiState.positionOptions.indexOf(uiState.selectedPosition).coerceAtLeast(0)
    val sideToMove = uiState.selectedPosition.gameState.sideToMove().name
        .lowercase()
        .replaceFirstChar(Char::uppercase)
    val orientationDescription = if (isBoardFlipped) {
        "Black pieces at the bottom."
    } else {
        "White pieces at the bottom."
    }

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
                    previewState = previewState,
                    accessibilityLabel = "${uiState.selectedPosition.label} chess board. $sideToMove to move. $orientationDescription",
                    modifier = Modifier
                        .width(boardWidth)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(AppSpacing.Section))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Section)
                ) {
                    AnalyzePositionSection(
                        selectedPosition = uiState.selectedPosition,
                        positionOptions = uiState.positionOptions,
                        positionIndex = positionIndex,
                        sideToMove = sideToMove,
                        isBoardFlipped = isBoardFlipped,
                        onPreviousClick = viewModel::selectPreviousPosition,
                        onNextClick = viewModel::selectNextPosition,
                        onPositionSelected = viewModel::selectPosition,
                        onFlipBoardClick = { isBoardFlipped = !isBoardFlipped }
                    )
                    AnalyzeExplanationSection(
                        isExpanded = isExplanationExpanded,
                        onToggle = { isExplanationExpanded = !isExplanationExpanded }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyzePositionSection(
    selectedPosition: PositionOption,
    positionOptions: List<PositionOption>,
    positionIndex: Int,
    sideToMove: String,
    isBoardFlipped: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPositionSelected: (PositionOption) -> Unit,
    onFlipBoardClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = selectedPosition.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Preset ${positionIndex + 1} of ${positionOptions.size} \u2022 $sideToMove to move",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousClick) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Previous preset position"
                    )
                }
                DropdownSetting(
                    selected = selectedPosition,
                    options = positionOptions,
                    optionLabel = { it.label },
                    onSelected = onPositionSelected,
                    accessibilityLabel = "Preset position",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onNextClick) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Next preset position"
                    )
                }
            }
            OutlinedButton(
                onClick = onFlipBoardClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.RotateRight,
                    contentDescription = null
                )
                Text(
                    text = if (isBoardFlipped) "Show White At Bottom" else "Show Black At Bottom",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun AnalyzeExplanationSection(
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clickable(onClick = onToggle)
                    .semantics { role = Role.Button }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About this page",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse explanation" else "Expand explanation"
                )
            }
            if (isExpanded) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "This is a minimal version of the Analyze page, the core workspace in the full application. The complete version lets users enter moves, analyze positions with a bundled third-party chess engine, and archive games for future study."
                    )
                    Text(
                        text = "This showcase demonstrates a small subset of the available board and piece customization options. Open the Settings page to adjust the board and piece appearance."
                    )
                }
            }
        }
    }
}
