package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme
import com.yaroslavlebid.mp3downloader.util.NumberConverter

@Composable
fun YouTubeVideoItem(item: YouTubeVideo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.secondary,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(164.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                model = item.thumbnailUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.h2,
                maxLines = 2,
                color = MaterialTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 14.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                VideoStatisticInfo(
                    iconId = R.drawable.views_icon,
                    unit = stringResource(R.string.views),
                    count = item.viewCount,
                    modifier = Modifier.padding(end = 32.dp)
                )
                VideoStatisticInfo(
                    iconId = R.drawable.likes_icon,
                    unit = stringResource(R.string.likes),
                    count = item.likeCount
                )
            }
        }
    }
}

@Composable
fun VideoStatisticInfo(modifier: Modifier = Modifier, iconId: Int, unit: String, count: Long) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Icon",
            tint = Color.Unspecified,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "${NumberConverter.convertNumber(count)} $unit",
            style = MaterialTheme.typography.body1
        )
    }
}

// Run this preview
@Preview
@Composable
fun YouTubeVideoItemPreview() {
    MP3DownloaderTheme {
        YouTubeVideoItem(
            item = YouTubeVideo(
                "id",
                "Big Buck Bunny 60fps 4K - Official Blender Foundation Short Film",
                "https://i3.ytimg.com/vi/erLk59H86ww/maxresdefault.jpg",
                1430000000,
                1400542,
                ""
            )
        )
    }
}