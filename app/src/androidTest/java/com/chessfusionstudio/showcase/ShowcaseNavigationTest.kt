package com.chessfusionstudio.showcase

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.math.max
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowcaseNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        clearSettings()
        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
    }

    @After
    fun tearDown() {
        clearSettings()
    }

    @Test
    fun analyzePresetWorkflowNavigatesAndUpdatesTheBoard() {
        composeRule.onNodeWithText("Analyze").performClick()
        composeRule.onAllNodesWithText("Start Position").assertCountEquals(2)

        composeRule.onNodeWithContentDescription("Next preset position").performClick()
        composeRule.onAllNodesWithText("Philidor Defense").assertCountEquals(2)

        composeRule.onNodeWithText("Show Black At Bottom").performClick()
        composeRule.onNodeWithText("Show White At Bottom").assertExists()

        composeRule.onNodeWithText("About this page").performClick()
        composeRule.onNodeWithText(
            "This is a minimal version of the Analyze page, the core workspace in the full application. The complete version lets users enter moves, analyze positions with a bundled third-party chess engine, and archive games for future study."
        ).assertExists()

        composeRule.onNodeWithContentDescription("Open settings").performClick()
        composeRule.onNodeWithText("Board Palette").assertExists()

        composeRule.onNodeWithContentDescription("Go back").performClick()
        composeRule.onAllNodesWithText("Philidor Defense").assertCountEquals(2)
    }

    @Test
    fun analyzeBoardRendersMultipleColors() {
        composeRule.onNodeWithText("Analyze").performClick()

        val image = composeRule.onNodeWithTag("showcase-board").captureToImage()
        val pixels = image.toPixelMap()
        val sampledColors = mutableSetOf<Int>()
        val xStep = max(1, pixels.width / 16)
        val yStep = max(1, pixels.height / 16)

        for (x in 0 until pixels.width step xStep) {
            for (y in 0 until pixels.height step yStep) {
                sampledColors += pixels[x, y].toArgb()
            }
        }

        assertTrue("Expected rendered board to contain several colors", sampledColors.size >= 4)
    }

    @Test
    fun settingsRestoreDefaults_requiresConfirmation() {
        composeRule.onNodeWithText("Settings").performClick()
        composeRule.onNodeWithText("Restore Defaults").performClick()

        composeRule.onNodeWithText("Restore default settings?").assertExists()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithText("Board Palette").assertExists()
    }

    private fun clearSettings() {
        context.getSharedPreferences("showcase_settings", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }
}
