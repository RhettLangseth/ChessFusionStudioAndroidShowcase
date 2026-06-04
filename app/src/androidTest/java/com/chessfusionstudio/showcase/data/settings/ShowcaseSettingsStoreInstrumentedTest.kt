package com.chessfusionstudio.showcase.data.settings

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowcaseSettingsStoreInstrumentedTest {
    @Test
    fun settingsPersistAcrossStoreInstances() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = context.getSharedPreferences("showcase_settings", Context.MODE_PRIVATE)
        preferences.edit().clear().commit()

        try {
            val firstStore = ShowcaseSettingsStore(context)
            firstStore.setPositionId(POSITION_RUY_LOPEZ)
            firstStore.setLightSquareArgb(0x00123456)
            firstStore.setPieceScale(0.72f)

            val restored = ShowcaseSettingsStore(context).settings.value

            assertEquals(POSITION_RUY_LOPEZ, restored.positionId)
            assertEquals(0xFF123456.toInt(), restored.lightSquareArgb)
            assertEquals(0.72f, restored.pieceScale, 0.001f)
        } finally {
            preferences.edit().clear().commit()
        }
    }

    @Test
    fun restoreAppearanceDefaults_preservesSelectedPosition() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = context.getSharedPreferences("showcase_settings", Context.MODE_PRIVATE)
        preferences.edit().clear().commit()

        try {
            val store = ShowcaseSettingsStore(context)
            store.setPositionId(POSITION_RUY_LOPEZ)
            store.setLightSquareArgb(0x00123456)
            store.setPieceScale(0.72f)

            store.restoreAppearanceDefaults()
            val restored = ShowcaseSettingsStore(context).settings.value

            assertEquals(POSITION_RUY_LOPEZ, restored.positionId)
            assertEquals(BOARD_PALETTE_WALNUT, restored.boardPaletteId)
            assertEquals(0xFFF0D9B5.toInt(), restored.lightSquareArgb)
            assertEquals(0.90f, restored.pieceScale, 0.001f)
        } finally {
            preferences.edit().clear().commit()
        }
    }
}
