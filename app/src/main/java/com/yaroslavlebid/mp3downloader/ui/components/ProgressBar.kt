package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.ui.theme.Blue
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme
import com.yaroslavlebid.mp3downloader.ui.theme.MidWhite
import com.yaroslavlebid.mp3downloader.ui.theme.RegularGray

@Composable
fun ProgressWidget(
    modifier: Modifier = Modifier,
    progress: Float = 0.0f,
    statusText: String = "",
    statusInfo: String = "",
    indicatorColor: Color = Blue
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(text = statusText, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = statusInfo, style = MaterialTheme.typography.body1, color = RegularGray)
        }
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .height(7.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small),
            backgroundColor = MidWhite,
            color = indicatorColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressWidgetPreview() {
    MP3DownloaderTheme {
        ProgressWidget(
            progress = 0.66f,
            statusText = "Downloading...",
            statusInfo = "2.56MB / 4.25MB"
        )
    }
}