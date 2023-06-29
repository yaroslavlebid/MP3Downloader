package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    isLoading: Boolean = false,
    text: String = ""
) {
    Button(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledBackgroundColor = MaterialTheme.colors.primaryVariant,
            disabledContentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(20.dp),
                strokeWidth = 1.dp,
                color = MaterialTheme.colors.onPrimary
            )
        }
        Text(text = text, style = MaterialTheme.typography.button)
    }
}

@Preview
@Composable
fun AppButtonPreview() {
    MP3DownloaderTheme {
        AppButton(onClick = { /*TODO*/ }, text = "Download")
    }
}

@Preview
@Composable
fun AppButtonPreviewLoading() {
    MP3DownloaderTheme {
        AppButton(onClick = { /*TODO*/ }, text = "Grabbing info...", isLoading = true)
    }
}