package com.chessfusionstudio.showcase.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
internal fun ChessCancunNoticeSection(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    ThemeStudioSection(title = "ChessCancun", modifier = modifier) {
        Text(
            text = "Bundled chess font used to render chess pieces.",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { uriHandler.openUri("https://github.com/samboy/ChessCancun") }) {
                Text("Project")
            }
            TextButton(onClick = { uriHandler.openUri("https://raw.githubusercontent.com/samboy/ChessCancun/main/COPYING.md") }) {
                Text("Details")
            }
        }
    }
}
