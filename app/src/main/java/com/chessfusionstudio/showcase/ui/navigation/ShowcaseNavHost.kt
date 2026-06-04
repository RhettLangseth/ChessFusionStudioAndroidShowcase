package com.chessfusionstudio.showcase.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.chessfusionstudio.showcase.ui.about.ShowcaseAboutRoute
import com.chessfusionstudio.showcase.ui.analyze.ShowcaseAnalyzeRoute
import com.chessfusionstudio.showcase.ui.mainmenu.ShowcaseStartScreen
import com.chessfusionstudio.showcase.ui.settings.ShowcaseSettingsRoute

@Composable
fun ShowcaseNavHost(onExit: () -> Unit) {
    var backStack by rememberSaveable {
        mutableStateOf(listOf(ShowcaseDestination.Start.name))
    }
    val currentDestination = ShowcaseDestination.fromName(backStack.lastOrNull().orEmpty())

    fun navigateTo(destination: ShowcaseDestination) {
        if (destination == currentDestination) {
            return
        }
        backStack = backStack + destination.name
    }

    fun popBack() {
        backStack = if (backStack.size > 1) {
            backStack.dropLast(1)
        } else {
            onExit()
            backStack
        }
    }

    BackHandler(enabled = currentDestination != ShowcaseDestination.Start) {
        popBack()
    }

    when (currentDestination) {
        ShowcaseDestination.Start -> ShowcaseStartScreen(
            onAnalyzeClick = { navigateTo(ShowcaseDestination.Analyze) },
            onSettingsClick = { navigateTo(ShowcaseDestination.Settings) },
            onAboutClick = { navigateTo(ShowcaseDestination.About) },
            onExitClick = onExit
        )

        ShowcaseDestination.Analyze -> ShowcaseAnalyzeRoute(
            onBackClick = ::popBack,
            onSettingsClick = { navigateTo(ShowcaseDestination.Settings) }
        )

        ShowcaseDestination.Settings -> ShowcaseSettingsRoute(onBackClick = ::popBack)

        ShowcaseDestination.About -> ShowcaseAboutRoute(onBackClick = ::popBack)
    }
}
