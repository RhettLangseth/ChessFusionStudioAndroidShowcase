package com.chessfusionstudio.showcase.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.chessfusionstudio.core.model.Piece
import com.chessfusionstudio.core.model.PieceType
import com.chessfusionstudio.core.model.Color as PieceColor
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class ShowcasePieceStyle(
    val backgroundFillColor: Color,
    val foregroundFillColor: Color,
    val scale: Float
)

internal object ShowcasePieceRenderer {
    private const val FontAssetPath = "fonts/ChessCancun.ttf"
    private const val DesignEm = 1000f
    private const val MinScale = 0.001f
    private const val MarginPx = 1
    private const val PieceRenderSupersampleFactor = 2
    private const val HoleMaskSupersampleFactor = 4
    private const val RenderVersion = 1

    private data class PieceBitmapKey(
        val type: PieceType,
        val color: PieceColor,
        val spriteSizePx: Int,
        val foregroundArgb: Int,
        val backgroundArgb: Int,
        val renderVersion: Int
    )

    private data class PieceLayoutKey(
        val type: PieceType,
        val color: PieceColor
    )

    private data class GlyphMetrics(
        val anchorX: Float,
        val top: Float,
        val bottom: Float,
        val halfWidth: Float
    )

    private data class SharedLayoutProfile(
        val metricsByPiece: Map<PieceLayoutKey, GlyphMetrics>,
        val tallestHeight: Float,
        val maxHalfWidth: Float
    )

    private val bitmapCache = ConcurrentHashMap<PieceBitmapKey, Bitmap>()
    private val maskRasterizer = ShowcasePieceMaskRasterizer(
        holeMaskSupersampleFactor = HoleMaskSupersampleFactor
    )
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        isFilterBitmap = true
    }

    @Volatile
    private var cachedLayoutProfile: SharedLayoutProfile? = null

    fun loadTypeface(context: Context): Typeface {
        return runCatching {
            Typeface.createFromAsset(context.assets, FontAssetPath)
        }.getOrDefault(Typeface.DEFAULT)
    }

    fun DrawScope.drawPiece(piece: Piece, rect: Rect, style: ShowcasePieceStyle, typeface: Typeface) {
        val spriteSize = min(rect.width, rect.height).roundToInt().coerceAtLeast(1)
        val bitmap = pieceBitmap(
            piece = piece,
            spriteSize = spriteSize,
            foregroundArgb = style.foregroundFillColor.toArgb(),
            backgroundArgb = style.backgroundFillColor.toArgb(),
            typeface = typeface
        )
        drawContext.canvas.nativeCanvas.drawBitmap(
            bitmap,
            null,
            RectF(rect.left, rect.top, rect.right, rect.bottom),
            bitmapPaint
        )
    }

    private fun pieceBitmap(
        piece: Piece,
        spriteSize: Int,
        foregroundArgb: Int,
        backgroundArgb: Int,
        typeface: Typeface
    ): Bitmap {
        val key = PieceBitmapKey(
            type = piece.type(),
            color = piece.color(),
            spriteSizePx = spriteSize,
            foregroundArgb = normalizeOpaqueArgb(foregroundArgb),
            backgroundArgb = normalizeOpaqueArgb(backgroundArgb),
            renderVersion = RenderVersion
        )
        bitmapCache[key]?.let { return it }

        val created = buildBitmap(
            piece = piece,
            spriteSize = spriteSize,
            foregroundArgb = key.foregroundArgb,
            backgroundArgb = key.backgroundArgb,
            typeface = typeface
        )
        val existing = bitmapCache.putIfAbsent(key, created)
        return existing ?: created
    }

    private fun buildBitmap(
        piece: Piece,
        spriteSize: Int,
        foregroundArgb: Int,
        backgroundArgb: Int,
        typeface: Typeface
    ): Bitmap {
        val renderSize = (spriteSize * PieceRenderSupersampleFactor).coerceAtLeast(1)
        val outlinePath = createAlignedOutlinePath(
            piece = piece,
            spriteSize = renderSize,
            typeface = typeface
        )
        val renderBitmap = Bitmap.createBitmap(renderSize, renderSize, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(renderBitmap)

        drawInteriorBackground(
            canvas = canvas,
            outlinePath = outlinePath,
            spriteSize = renderSize,
            backgroundArgb = backgroundArgb
        )
        drawForeground(
            canvas = canvas,
            outlinePath = outlinePath,
            foregroundArgb = foregroundArgb
        )

        if (renderSize == spriteSize) {
            return renderBitmap
        }
        return Bitmap.createScaledBitmap(
            renderBitmap,
            spriteSize,
            spriteSize,
            true
        ).also { scaled ->
            if (scaled !== renderBitmap) {
                renderBitmap.recycle()
            }
        }
    }

    private fun drawInteriorBackground(
        canvas: AndroidCanvas,
        outlinePath: Path,
        spriteSize: Int,
        backgroundArgb: Int
    ) {
        val holeMap = maskRasterizer.buildInteriorHoleComponentMap(
            outlinePath = outlinePath,
            spriteSize = spriteSize
        )
        if (holeMap.componentCount == 0) {
            return
        }
        val holeMask = maskRasterizer.createInteriorHoleMaskBitmap(
            map = holeMap,
            spriteSize = spriteSize
        ) ?: return
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            color = backgroundArgb
        }
        val layer = canvas.saveLayer(
            0f,
            0f,
            spriteSize.toFloat(),
            spriteSize.toFloat(),
            null
        )
        canvas.drawRect(0f, 0f, spriteSize.toFloat(), spriteSize.toFloat(), backgroundPaint)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            isFilterBitmap = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
        canvas.drawBitmap(holeMask, 0f, 0f, maskPaint)
        maskPaint.xfermode = null
        canvas.restoreToCount(layer)
        holeMask.recycle()
    }

    private fun drawForeground(
        canvas: AndroidCanvas,
        outlinePath: Path,
        foregroundArgb: Int
    ) {
        val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            color = foregroundArgb
        }
        canvas.drawPath(outlinePath, foregroundPaint)
    }

    private fun createAlignedOutlinePath(
        piece: Piece,
        spriteSize: Int,
        typeface: Typeface
    ): Path {
        val margin = MarginPx.coerceIn(0, spriteSize / 2)
        val interior = max(0, spriteSize - 2 * margin)
        val profile = sharedLayoutProfile(typeface)
        val pieceKey = PieceLayoutKey(type = piece.type(), color = piece.color())
        val glyphPaint = createDesignGlyphPaint(typeface)
        val glyphPath = buildGlyphPath(
            character = ShowcasePieceGlyphs.glyphFor(piece),
            glyphPaint = glyphPaint
        )
        val fallbackBounds = RectF().also { bounds ->
            glyphPath.computeBounds(bounds, true)
        }
        val pieceMetrics = profile.metricsByPiece[pieceKey] ?: GlyphMetrics(
            anchorX = fallbackBounds.centerX(),
            top = fallbackBounds.top,
            bottom = fallbackBounds.bottom,
            halfWidth = max(
                abs(fallbackBounds.left - fallbackBounds.centerX()),
                abs(fallbackBounds.right - fallbackBounds.centerX())
            )
        )

        val centerX = margin + interior / 2f
        val centerY = margin + interior / 2f
        val halfInterior = interior / 2f
        val widthScaleLimit = if (profile.maxHalfWidth > 0f) {
            halfInterior / profile.maxHalfWidth
        } else {
            Float.POSITIVE_INFINITY
        }
        val verticalScaleLimit = if (profile.tallestHeight > 0f) {
            interior / profile.tallestHeight
        } else {
            Float.POSITIVE_INFINITY
        }
        val scale = min(widthScaleLimit, verticalScaleLimit)
            .takeIf { candidate -> candidate.isFinite() && candidate > 0f }
            ?: MinScale
        val tallestBottomY = centerY + (profile.tallestHeight * scale * 0.5f)
        val baselineY = tallestBottomY - (pieceMetrics.bottom * scale)

        return Path().apply {
            glyphPath.transform(Matrix().apply {
                postTranslate(-pieceMetrics.anchorX, 0f)
                postScale(max(MinScale, scale), max(MinScale, scale))
                postTranslate(centerX, baselineY)
            }, this)
            fillType = Path.FillType.EVEN_ODD
        }
    }

    private fun sharedLayoutProfile(typeface: Typeface): SharedLayoutProfile {
        cachedLayoutProfile?.let { return it }
        synchronized(this) {
            cachedLayoutProfile?.let { return it }
            return buildSharedLayoutProfile(typeface).also { cachedLayoutProfile = it }
        }
    }

    private fun buildSharedLayoutProfile(typeface: Typeface): SharedLayoutProfile {
        val glyphPaint = createDesignGlyphPaint(typeface)
        val metricsByPiece = linkedMapOf<PieceLayoutKey, GlyphMetrics>()
        PieceColor.values().forEach { color ->
            PieceType.values().forEach { type ->
                val glyphPath = buildGlyphPath(
                    character = ShowcasePieceGlyphs.glyphFor(type, color).toString(),
                    glyphPaint = glyphPaint
                )
                val bounds = RectF().also { rect ->
                    glyphPath.computeBounds(rect, true)
                }
                val anchorX = bounds.centerX()
                metricsByPiece[PieceLayoutKey(type = type, color = color)] = GlyphMetrics(
                    anchorX = anchorX,
                    top = bounds.top,
                    bottom = bounds.bottom,
                    halfWidth = max(
                        abs(bounds.left - anchorX),
                        abs(bounds.right - anchorX)
                    )
                )
            }
        }

        val fallbackMetrics = GlyphMetrics(
            anchorX = 0f,
            top = -DesignEm / 2f,
            bottom = DesignEm / 2f,
            halfWidth = DesignEm / 2f
        )
        val allMetrics = metricsByPiece.values.ifEmpty { listOf(fallbackMetrics) }
        return SharedLayoutProfile(
            metricsByPiece = metricsByPiece,
            tallestHeight = allMetrics.maxOfOrNull { metrics -> metrics.bottom - metrics.top } ?: DesignEm,
            maxHalfWidth = allMetrics.maxOfOrNull { metrics -> metrics.halfWidth } ?: (DesignEm / 2f)
        )
    }

    private fun createDesignGlyphPaint(typeface: Typeface): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            this.typeface = typeface
            style = Paint.Style.FILL
            textSize = DesignEm
            isAntiAlias = true
            isDither = true
            isSubpixelText = true
            hinting = Paint.HINTING_ON
        }
    }

    private fun buildGlyphPath(character: String, glyphPaint: Paint): Path {
        return Path().apply {
            glyphPaint.getTextPath(character, 0, character.length, 0f, 0f, this)
            fillType = Path.FillType.EVEN_ODD
        }
    }

    private fun normalizeOpaqueArgb(argb: Int): Int {
        return (argb and 0x00FFFFFF) or (0xFF shl 24)
    }
}
