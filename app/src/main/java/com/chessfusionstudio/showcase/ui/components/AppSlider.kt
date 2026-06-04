package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

sealed interface AppSliderTrackFill {
    data object ThemeDefault : AppSliderTrackFill
    data class Solid(val color: Color) : AppSliderTrackFill
    data class Gradient(val colors: List<Color>) : AppSliderTrackFill
}

data class AppSliderVisualStyle(
    val minContainerHeight: androidx.compose.ui.unit.Dp = 20.dp,
    val barHeight: androidx.compose.ui.unit.Dp = 20.dp,
    val trackVerticalPadding: androidx.compose.ui.unit.Dp = 0.dp,
    val trackCornerRadius: androidx.compose.ui.unit.Dp = 6.dp,
    val knobOverflow: androidx.compose.ui.unit.Dp = 3.dp,
    val knobBodyWidth: androidx.compose.ui.unit.Dp = 12.dp,
    val knobOuterStrokeWidth: androidx.compose.ui.unit.Dp = 3.dp,
    val knobInnerStrokeWidth: androidx.compose.ui.unit.Dp = 1.dp,
    val knobVerticalOverflow: androidx.compose.ui.unit.Dp = knobOuterStrokeWidth / 2f,
    val knobOuterColor: Color = Color.White,
    val knobInnerColor: Color = Color.Black.copy(alpha = 0.45f),
    val trackBorderColor: Color = Color.Black.copy(alpha = 0.18f)
)

object AppSliderDefaults {
    val DefaultSliderHeight = 20.dp

    val ColorPickerVisualStyle = AppSliderVisualStyle(
        minContainerHeight = 40.dp,
        barHeight = 40.dp
    )

    @Composable
    fun themeVisualStyle(): AppSliderVisualStyle {
        return AppSliderVisualStyle(
            minContainerHeight = DefaultSliderHeight,
            barHeight = DefaultSliderHeight,
            trackBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.72f)
        )
    }
}

@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    trackFill: AppSliderTrackFill = AppSliderTrackFill.ThemeDefault,
    showValueBubble: Boolean = false,
    valueLabel: ((Float) -> String)? = null,
    style: AppSliderVisualStyle = AppSliderDefaults.themeVisualStyle(),
    onInteractionStart: (() -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    accessibilityLabel: String? = null
) {
    val rangeStart = minOf(valueRange.start, valueRange.endInclusive)
    val rangeEnd = maxOf(valueRange.start, valueRange.endInclusive)
    val span = (rangeEnd - rangeStart).coerceAtLeast(0f)
    val normalizedValue = if (span <= 0f) {
        0f
    } else {
        ((value - rangeStart) / span).coerceIn(0f, 1f)
    }

    AppSliderNormalized(
        normalizedValue = normalizedValue,
        onNormalizedValueChange = { normalized ->
            val resolvedValue = if (span <= 0f) {
                rangeStart
            } else {
                rangeStart + (normalized * span)
            }
            onValueChange(resolvedValue)
        },
        trackFill = trackFill,
        showValueBubble = showValueBubble,
        bubbleText = valueLabel?.invoke(value) ?: defaultContinuousValueLabel(value),
        style = style,
        onInteractionStart = onInteractionStart,
        onValueChangeFinished = onValueChangeFinished,
        accessibilityLabel = accessibilityLabel,
        modifier = modifier
    )
}

@Composable
private fun AppSliderNormalized(
    normalizedValue: Float,
    onNormalizedValueChange: (Float) -> Unit,
    trackFill: AppSliderTrackFill,
    showValueBubble: Boolean,
    bubbleText: String,
    style: AppSliderVisualStyle,
    onInteractionStart: (() -> Unit)?,
    onValueChangeFinished: (() -> Unit)?,
    accessibilityLabel: String?,
    modifier: Modifier = Modifier
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var isInteracting by remember { mutableStateOf(false) }
    val onNormalizedValueChangeState = rememberUpdatedState(onNormalizedValueChange)
    val onInteractionStartState = rememberUpdatedState(onInteractionStart)
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val density = LocalDensity.current
    val knobOverflowPx = with(density) { style.knobOverflow.toPx() }
    val knobBodyWidthPx = with(density) { style.knobBodyWidth.toPx() }
    val knobVerticalOverflowPx = with(density) { style.knobVerticalOverflow.toPx() }
    val barHeightPx = with(density) { style.barHeight.toPx() }
    val resolvedNormalizedValue = normalizedValue.coerceIn(0f, 1f)
    val fallbackTrackColor = SwitchDefaults.colors().checkedTrackColor
    val bubbleGapPx = with(density) { 4.dp.roundToPx() }

    fun updateFromOffset(offset: Offset) {
        val canvasWidth = canvasSize.width.toFloat().coerceAtLeast(1f)
        val resolvedKnobWidthPx = knobBodyWidthPx.coerceAtMost(canvasWidth)
        val knobTravel = canvasWidth - resolvedKnobWidthPx
        if (knobTravel <= 0f) {
            onNormalizedValueChange(0f)
            return
        }
        val knobLeft = (offset.x - (resolvedKnobWidthPx / 2f)).coerceIn(0f, knobTravel)
        val normalized = (knobLeft / knobTravel).coerceIn(0f, 1f)
        onNormalizedValueChangeState.value(normalized)
    }

    fun resolveTrackGeometry(containerHeightPx: Float): Pair<Float, Float> {
        val resolvedContainerHeight = containerHeightPx.coerceAtLeast(1f)
        val verticalPaddingPx = with(density) {
            style.trackVerticalPadding.toPx().coerceAtLeast(0f)
        }
        val maxTrackHeight = (
            resolvedContainerHeight - (verticalPaddingPx * 2f)
            ).coerceAtLeast(1f)
        val resolvedTrackHeight = barHeightPx
            .coerceAtLeast(1f)
            .coerceAtMost(maxTrackHeight)
        val trackTop = verticalPaddingPx + ((maxTrackHeight - resolvedTrackHeight) / 2f)
        return trackTop to resolvedTrackHeight
    }

    BoxWithConstraints(
        modifier = Modifier
            .height(style.minContainerHeight)
            .then(modifier)
            .zIndex(1f)
            .semantics {
                if (accessibilityLabel != null) {
                    contentDescription = accessibilityLabel
                }
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = resolvedNormalizedValue,
                    range = 0f..1f,
                    steps = 0
                )
                setProgress { targetValue ->
                    onNormalizedValueChangeState.value(targetValue.coerceIn(0f, 1f))
                    true
                }
            }
            .onSizeChanged { canvasSize = it }
            .pointerInput(canvasSize, style) {
                awaitEachGesture {
                    val firstDown = awaitFirstDown(
                        requireUnconsumed = false
                    )
                    var activePointerId = firstDown.id
                    var interactionStarted = false
                    fun ensureInteractionStarted() {
                        if (!interactionStarted) {
                            interactionStarted = true
                            isInteracting = true
                            onInteractionStartState.value?.invoke()
                        }
                    }
                    ensureInteractionStarted()
                    updateFromOffset(firstDown.position)
                    firstDown.consume()
                    try {
                        while (true) {
                            val event = awaitPointerEvent()
                            val activeChange = event.changes.firstOrNull { change ->
                                change.id == activePointerId
                            } ?: event.changes.firstOrNull { change ->
                                change.pressed
                            }
                            if (activeChange == null) {
                                break
                            }
                            activePointerId = activeChange.id
                            updateFromOffset(activeChange.position)
                            if (activeChange.positionChanged()) {
                                activeChange.consume()
                            }
                            if (!activeChange.pressed || event.changes.none { change -> change.pressed }) {
                                activeChange.consume()
                                break
                            }
                        }
                    } finally {
                        isInteracting = false
                        onValueChangeFinishedState.value?.invoke()
                    }
                }
            }
    ) {
        val fallbackCanvasWidthPx = with(density) {
            if (maxWidth.value.isFinite()) {
                maxWidth.roundToPx().coerceAtLeast(1)
            } else {
                1
            }
        }
        val fallbackCanvasHeightPx = with(density) {
            if (maxHeight.value.isFinite()) {
                maxHeight.roundToPx().coerceAtLeast(1)
            } else {
                style.minContainerHeight.roundToPx().coerceAtLeast(1)
            }
        }
        val resolvedCanvasWidthPx = if (canvasSize.width > 0) {
            canvasSize.width
        } else {
            fallbackCanvasWidthPx
        }
        val resolvedCanvasHeightPx = if (canvasSize.height > 0) {
            canvasSize.height
        } else {
            fallbackCanvasHeightPx
        }
        val overlayWidth = with(density) {
            (resolvedCanvasWidthPx.toFloat() + (knobOverflowPx * 2f)).toDp()
        }
        val overlayHeight = with(density) {
            (resolvedCanvasHeightPx.toFloat() + (knobVerticalOverflowPx * 2f)).toDp()
        }
        val knobCenterXInSliderPx = run {
            val sliderWidth = resolvedCanvasWidthPx.toFloat().coerceAtLeast(1f)
            val resolvedKnobWidthPx = knobBodyWidthPx.coerceAtMost(sliderWidth)
            val knobRange = (sliderWidth - resolvedKnobWidthPx).coerceAtLeast(0f)
            (resolvedNormalizedValue * knobRange) + (resolvedKnobWidthPx / 2f)
        }
        val trackTopPx = resolveTrackGeometry(resolvedCanvasHeightPx.toFloat()).first
        val bubblePositionProvider = remember(knobCenterXInSliderPx, trackTopPx, bubbleGapPx) {
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    val desiredX = anchorBounds.left +
                        knobCenterXInSliderPx.roundToInt() -
                        (popupContentSize.width / 2)
                    val desiredY = anchorBounds.top +
                        trackTopPx.roundToInt() -
                        popupContentSize.height -
                        bubbleGapPx
                    val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
                    val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
                    return IntOffset(
                        x = desiredX.coerceIn(0, maxX),
                        y = desiredY.coerceIn(0, maxY)
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        ) {
            val (trackTop, trackHeight) = resolveTrackGeometry(size.height)
            val trackCorner = style.trackCornerRadius.toPx()
            val resolvedKnobWidthPx = knobBodyWidthPx.coerceAtMost(size.width)
            val knobRange = (size.width - resolvedKnobWidthPx).coerceAtLeast(0f)
            val knobLeft = resolvedNormalizedValue * knobRange
            val themeDefaultActiveWidth = (knobLeft + resolvedKnobWidthPx)
                .coerceIn(0f, size.width)

            when (trackFill) {
                is AppSliderTrackFill.Solid -> {
                    drawRoundRect(
                        color = trackFill.color,
                        topLeft = Offset(0f, trackTop),
                        size = Size(size.width, trackHeight),
                        cornerRadius = CornerRadius(trackCorner, trackCorner)
                    )
                    drawRoundRect(
                        color = style.trackBorderColor,
                        topLeft = Offset(0f, trackTop),
                        size = Size(size.width, trackHeight),
                        cornerRadius = CornerRadius(trackCorner, trackCorner),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                is AppSliderTrackFill.Gradient -> {
                    val colors = trackFill.colors
                    if (colors.size >= 2) {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(colors = colors),
                            topLeft = Offset(0f, trackTop),
                            size = Size(size.width, trackHeight),
                            cornerRadius = CornerRadius(trackCorner, trackCorner)
                        )
                    } else {
                        drawRoundRect(
                            color = colors.firstOrNull() ?: fallbackTrackColor,
                            topLeft = Offset(0f, trackTop),
                            size = Size(size.width, trackHeight),
                            cornerRadius = CornerRadius(trackCorner, trackCorner)
                        )
                    }
                    drawRoundRect(
                        color = style.trackBorderColor,
                        topLeft = Offset(0f, trackTop),
                        size = Size(size.width, trackHeight),
                        cornerRadius = CornerRadius(trackCorner, trackCorner),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                AppSliderTrackFill.ThemeDefault -> {
                    if (themeDefaultActiveWidth > 0f) {
                        drawRoundRect(
                            color = fallbackTrackColor,
                            topLeft = Offset(0f, trackTop),
                            size = Size(themeDefaultActiveWidth, trackHeight),
                            cornerRadius = CornerRadius(trackCorner, trackCorner)
                        )
                    }
                    drawRoundRect(
                        color = style.trackBorderColor,
                        topLeft = Offset(0f, trackTop),
                        size = Size(size.width, trackHeight),
                        cornerRadius = CornerRadius(trackCorner, trackCorner),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = -style.knobOverflow, y = -style.knobVerticalOverflow)
                .width(overlayWidth)
                .height(overlayHeight)
        ) {
            val (baseTrackTop, trackHeight) = resolveTrackGeometry(
                resolvedCanvasHeightPx.toFloat()
            )
            val trackTop = baseTrackTop + knobVerticalOverflowPx
            val trackCorner = style.trackCornerRadius.toPx()
            val whiteStroke = style.knobOuterStrokeWidth.toPx()
            val blackStroke = style.knobInnerStrokeWidth.toPx()

            val parentRowWidth = resolvedCanvasWidthPx.toFloat().coerceAtLeast(1f)
            val blackOuterWidth = knobBodyWidthPx.coerceAtMost(parentRowWidth)
            val knobRange = (parentRowWidth - blackOuterWidth).coerceAtLeast(0f)
            val blackOuterLeft = knobOverflowPx + (resolvedNormalizedValue * knobRange)
            val blackOuterTop = trackTop
            val blackOuterHeight = trackHeight

            val whitePathLeft = blackOuterLeft - (whiteStroke / 2f)
            val whitePathTop = blackOuterTop - (whiteStroke / 2f)
            val whitePathWidth = blackOuterWidth + whiteStroke
            val whitePathHeight = blackOuterHeight + whiteStroke
            val whiteCorner = trackCorner + (whiteStroke / 2f)

            val blackPathLeft = blackOuterLeft + (blackStroke / 2f)
            val blackPathTop = blackOuterTop + (blackStroke / 2f)
            val blackPathWidth = (blackOuterWidth - blackStroke).coerceAtLeast(1f)
            val blackPathHeight = (blackOuterHeight - blackStroke).coerceAtLeast(1f)
            val blackCorner = (trackCorner - (blackStroke / 2f)).coerceAtLeast(0f)

            drawRoundRect(
                color = style.knobOuterColor,
                topLeft = Offset(whitePathLeft, whitePathTop),
                size = Size(whitePathWidth, whitePathHeight),
                cornerRadius = CornerRadius(whiteCorner, whiteCorner),
                style = Stroke(width = whiteStroke)
            )
            drawRoundRect(
                color = style.knobInnerColor,
                topLeft = Offset(blackPathLeft, blackPathTop),
                size = Size(blackPathWidth, blackPathHeight),
                cornerRadius = CornerRadius(blackCorner, blackCorner),
                style = Stroke(width = blackStroke)
            )
        }

        if (showValueBubble && isInteracting) {
            Popup(
                popupPositionProvider = bubblePositionProvider
            ) {
                SliderValueBubble(text = bubbleText)
            }
        }
    }
}

@Composable
private fun SliderValueBubble(
    text: String
) {
    val bubbleColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bubbleColor)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Canvas(modifier = Modifier.size(width = 10.dp, height = 6.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2f, size.height)
                lineTo(0f, 0f)
                lineTo(size.width, 0f)
                close()
            }
            drawPath(
                path = path,
                color = bubbleColor
            )
        }
    }
}

private fun defaultContinuousValueLabel(value: Float): String {
    val rounded = value.roundToInt()
    return if (abs(value - rounded.toFloat()) < 0.001f) {
        rounded.toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}

