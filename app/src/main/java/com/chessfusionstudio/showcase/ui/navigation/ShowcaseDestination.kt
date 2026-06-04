package com.chessfusionstudio.showcase.ui.navigation

enum class ShowcaseDestination(val showsTopBar: Boolean) {
    Start(showsTopBar = false),
    Analyze(showsTopBar = true),
    Settings(showsTopBar = true),
    About(showsTopBar = true);

    companion object {
        fun fromName(value: String): ShowcaseDestination {
            return entries.firstOrNull { it.name == value } ?: Start
        }
    }
}
