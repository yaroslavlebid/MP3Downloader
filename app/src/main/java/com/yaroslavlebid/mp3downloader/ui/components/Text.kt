package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.ui.theme.RegularGray

@Composable
fun InfoText(text: String, modifier: Modifier = Modifier, leadingIconId: Int? = null) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIconId != null) {
            Icon(
                painter = painterResource(id = leadingIconId),
                contentDescription = stringResource(
                    R.string.info_icon_content_description
                ),
                modifier = Modifier.padding(end = 4.dp),
                tint = Color.Unspecified
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            color = RegularGray
        )
    }
}