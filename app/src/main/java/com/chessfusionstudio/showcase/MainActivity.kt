package com.chessfusionstudio.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chessfusionstudio.showcase.ui.showcase.ThemeStudioScreen
import com.chessfusionstudio.showcase.ui.theme.ShowcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                ThemeStudioScreen()
            }
        }
    }
}
