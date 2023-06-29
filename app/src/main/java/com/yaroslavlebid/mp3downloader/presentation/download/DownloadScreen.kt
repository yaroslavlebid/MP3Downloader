package com.yaroslavlebid.mp3downloader.presentation.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.ui.components.AppButton
import com.yaroslavlebid.mp3downloader.ui.components.AppTopBar
import com.yaroslavlebid.mp3downloader.ui.components.InfoText
import com.yaroslavlebid.mp3downloader.ui.components.ProgressWidget
import com.yaroslavlebid.mp3downloader.ui.components.YouTubeVideoItem

@Composable
fun DownloadScreen(
    state: DownloadScreenState,
    modifier: Modifier = Modifier,
    onLoadNext: () -> Unit = { }
) {

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 60.dp)
        ) {
            AppTopBar()
            if (state.item != null) {
                YouTubeVideoItem(item = state.item, modifier = Modifier.padding(bottom = 24.dp))
                ProgressWidget(
                    statusText = stringResource(id = state.downloadState.status),
                    statusInfo = state.downloadState.statusInfo,
                    progress = state.downloadState.progress,
                    indicatorColor = state.downloadState.color
                )
                if (state.downloadState is DownloadState.Success || state.downloadState is DownloadState.Failure) {
                    InfoText(
                        modifier = Modifier.padding(top = 14.dp),
                        text = when (state.downloadState) {
                            is DownloadState.Success -> {
                                state.downloadState.message
                            }
                            is DownloadState.Failure -> {
                                state.downloadState.error
                            }
                            else -> ""
                        },
                        leadingIconId = when (state.downloadState) {
                            is DownloadState.Success -> {
                                R.drawable.success_icon
                            }
                            is DownloadState.Failure -> {
                                R.drawable.error_ic
                            }
                            else -> null
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AppButton(
                        text = stringResource(R.string.download_another_mp3),
                        onClick = onLoadNext
                    )
                }
            }
        }
    }
}