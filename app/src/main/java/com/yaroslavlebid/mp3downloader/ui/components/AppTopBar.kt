package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.youtube_logo),
            contentDescription = stringResource(
                R.string.youtube_logo
            ),
            modifier = Modifier
                .padding(top = 6.dp, end = 14.dp)
                .height(16.dp)
                .width(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.h1
        )
    }
}

@Preview
@Composable
fun AppTopBarPreview() {
    MP3DownloaderTheme {
        AppTopBar()
    }
}