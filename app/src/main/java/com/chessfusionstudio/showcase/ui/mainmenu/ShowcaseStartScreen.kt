package com.chessfusionstudio.showcase.ui.mainmenu

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.ui.components.AppSpacing

private const val LightBackgroundAsset = "ChessFusion/Backgrounds/Light.jpg"
private const val LightLogoAsset = "ChessFusion/Logos/Light.png"
private const val PortraitLogoTopFraction = 0.1766667f
private const val PortraitLogoSizeFraction = 0.3175f
private const val PortraitMenuTopFraction = 0.5058333f

@Composable
internal fun ShowcaseStartScreen(
    onAnalyzeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundBitmap = rememberAssetBitmap(LightBackgroundAsset)
    val logoBitmap = rememberAssetBitmap(LightLogoAsset)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        backgroundBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        logoBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = "Chess Fusion Studio logo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = maxHeight * PortraitLogoTopFraction)
                    .size(maxHeight * PortraitLogoSizeFraction),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = maxHeight * PortraitMenuTopFraction)
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section)
        ) {
            ShowcaseMenuButton(
                text = "Analyze",
                imageVector = Icons.Filled.School,
                onClick = onAnalyzeClick
            )
            ShowcaseMenuButton(
                text = "Settings",
                imageVector = Icons.Filled.Settings,
                onClick = onSettingsClick
            )
            ShowcaseMenuButton(
                text = "About",
                imageVector = Icons.Filled.Info,
                onClick = onAboutClick
            )
            ShowcaseMenuButton(
                text = "Exit",
                imageVector = Icons.AutoMirrored.Filled.Logout,
                onClick = onExitClick
            )
        }
    }
}

@Composable
private fun rememberAssetBitmap(assetPath: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(context, assetPath) {
        runCatching {
            context.assets.open(assetPath).use(BitmapFactory::decodeStream)?.asImageBitmap()
        }.getOrNull()
    }
}

@Composable
private fun ShowcaseMenuButton(
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
    }
}
