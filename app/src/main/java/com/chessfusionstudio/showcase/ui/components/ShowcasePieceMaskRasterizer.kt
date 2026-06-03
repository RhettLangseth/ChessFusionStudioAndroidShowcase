package com.chessfusionstudio.showcase.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path

internal class ShowcasePieceMaskRasterizer(
    private val holeMaskSupersampleFactor: Int
) {
    class HoleComponentMap(
        val width: Int,
        val height: Int,
        val componentIds: IntArray,
        val componentCount: Int,
        val componentPixelCounts: IntArray
    )

    fun buildInteriorHoleComponentMap(
        outlinePath: Path,
        spriteSize: Int
    ): HoleComponentMap {
        val supersample = holeMaskSupersampleFactor.coerceAtLeast(1)
        val maskSize = (spriteSize * supersample).coerceAtLeast(1)
        val filledMask = alphaMaskToFilledMask(
            alphaMask = renderPathToAlphaMask(
                path = outlinePath,
                maskSize = maskSize,
                supersample = supersample
            )
        )
        val externalMask = computeBorderConnectedEmptyMask(
            filledMask = filledMask,
            width = maskSize,
            height = maskSize
        )
        val interiorMask = BooleanArray(maskSize * maskSize)
        for (index in interiorMask.indices) {
            interiorMask[index] = !filledMask[index] && !externalMask[index]
        }
        return labelConnectedComponents(
            mask = interiorMask,
            width = maskSize,
            height = maskSize
        )
    }

    fun createInteriorHoleMaskBitmap(
        map: HoleComponentMap,
        spriteSize: Int
    ): Bitmap? {
        if (map.componentIds.isEmpty() || map.width <= 0 || map.height <= 0) {
            return null
        }
        val maskPixels = IntArray(map.width * map.height)
        var hasAnyPixel = false
        for (index in maskPixels.indices) {
            if (map.componentIds[index] != 0) {
                maskPixels[index] = Color.WHITE
                hasAnyPixel = true
            }
        }
        if (!hasAnyPixel) {
            return null
        }

        val highResolutionMask = Bitmap.createBitmap(map.width, map.height, Bitmap.Config.ARGB_8888).apply {
            setPixels(maskPixels, 0, map.width, 0, 0, map.width, map.height)
        }
        if (map.width == spriteSize && map.height == spriteSize) {
            return highResolutionMask
        }
        return Bitmap.createScaledBitmap(
            highResolutionMask,
            spriteSize,
            spriteSize,
            true
        ).also { scaled ->
            if (scaled !== highResolutionMask) {
                highResolutionMask.recycle()
            }
        }
    }

    private fun renderPathToAlphaMask(
        path: Path,
        maskSize: Int,
        supersample: Int
    ): IntArray {
        val scaledPath = Path(path)
        if (supersample != 1) {
            scaledPath.transform(Matrix().apply {
                setScale(supersample.toFloat(), supersample.toFloat())
            })
        }

        val bitmap = Bitmap.createBitmap(maskSize, maskSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            isAntiAlias = true
            isDither = true
        }
        canvas.drawPath(scaledPath, fillPaint)

        val pixels = IntArray(maskSize * maskSize)
        bitmap.getPixels(pixels, 0, maskSize, 0, 0, maskSize, maskSize)
        bitmap.recycle()
        return IntArray(pixels.size) { index -> (pixels[index] ushr 24) and 0xFF }
    }

    private fun alphaMaskToFilledMask(alphaMask: IntArray): BooleanArray {
        return BooleanArray(alphaMask.size) { index -> alphaMask[index] > 0 }
    }

    private fun computeBorderConnectedEmptyMask(
        filledMask: BooleanArray,
        width: Int,
        height: Int
    ): BooleanArray {
        val externalMask = BooleanArray(width * height)
        val queue = IntArray(width * height)
        var head = 0
        var tail = 0

        fun enqueue(index: Int) {
            if (!filledMask[index] && !externalMask[index]) {
                externalMask[index] = true
                queue[tail++] = index
            }
        }

        for (x in 0 until width) {
            enqueue(x)
            enqueue((height - 1) * width + x)
        }
        for (y in 1 until (height - 1).coerceAtLeast(1)) {
            enqueue(y * width)
            enqueue(y * width + (width - 1))
        }

        while (head < tail) {
            val index = queue[head++]
            val x = index % width
            val y = index / width

            if (x > 0) enqueue(index - 1)
            if (x < width - 1) enqueue(index + 1)
            if (y > 0) enqueue(index - width)
            if (y < height - 1) enqueue(index + width)
        }

        return externalMask
    }

    private fun labelConnectedComponents(
        mask: BooleanArray,
        width: Int,
        height: Int
    ): HoleComponentMap {
        val componentIds = IntArray(width * height)
        val componentMinX = mutableListOf<Int>()
        val componentMinY = mutableListOf<Int>()
        val queue = IntArray(width * height)
        var componentCount = 0

        for (index in mask.indices) {
            if (!mask[index] || componentIds[index] != 0) {
                continue
            }
            componentCount += 1
            var minX = width
            var minY = height
            var head = 0
            var tail = 0
            componentIds[index] = componentCount
            queue[tail++] = index

            while (head < tail) {
                val current = queue[head++]
                val x = current % width
                val y = current / width
                if (x < minX) minX = x
                if (y < minY) minY = y

                fun push(neighbor: Int) {
                    if (mask[neighbor] && componentIds[neighbor] == 0) {
                        componentIds[neighbor] = componentCount
                        queue[tail++] = neighbor
                    }
                }

                if (x > 0) push(current - 1)
                if (x < width - 1) push(current + 1)
                if (y > 0) push(current - width)
                if (y < height - 1) push(current + width)
            }

            componentMinX += minX
            componentMinY += minY
        }

        if (componentCount == 0) {
            return HoleComponentMap(
                width = width,
                height = height,
                componentIds = componentIds,
                componentCount = 0,
                componentPixelCounts = IntArray(1)
            )
        }

        val sortedOldIds = List(componentCount) { it + 1 }.sortedWith(
            compareBy<Int> { oldId -> componentMinY[oldId - 1] }
                .thenBy { oldId -> componentMinX[oldId - 1] }
        )
        val remap = IntArray(componentCount + 1)
        sortedOldIds.forEachIndexed { sortedIndex, oldId ->
            remap[oldId] = sortedIndex + 1
        }

        val sortedComponentIds = IntArray(componentIds.size)
        val sortedPixelCounts = IntArray(componentCount + 1)
        for (index in componentIds.indices) {
            val oldId = componentIds[index]
            if (oldId != 0) {
                val newId = remap[oldId]
                sortedComponentIds[index] = newId
                sortedPixelCounts[newId] += 1
            }
        }

        return HoleComponentMap(
            width = width,
            height = height,
            componentIds = sortedComponentIds,
            componentCount = componentCount,
            componentPixelCounts = sortedPixelCounts
        )
    }
}
