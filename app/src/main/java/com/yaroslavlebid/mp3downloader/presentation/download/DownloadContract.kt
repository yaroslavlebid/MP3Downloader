package com.yaroslavlebid.mp3downloader.presentation.download

import androidx.compose.ui.graphics.Color
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.ui.theme.Blue
import com.yaroslavlebid.mp3downloader.ui.theme.Green
import com.yaroslavlebid.mp3downloader.ui.theme.Orange
import com.yaroslavlebid.mp3downloader.ui.theme.Red
import com.yaroslavlebid.mp3downloader.ui.theme.SeaGreen

data class DownloadScreenState(
    val downloadState: DownloadState = DownloadState.Downloading(0.0f, "0 / 0"),
    val item: YouTubeVideo? = null
)

sealed class DownloadState(val progress: Float, val status: Int, val color: Color, val statusInfo: String) {
    class Downloading(progress: Float, statusInfo: String) : DownloadState(progress, R.string.downloading, Blue, statusInfo)
    class Converting(progress: Float, statusInfo: String) : DownloadState(progress, R.string.converting, Orange, statusInfo)
    class Saving(progress: Float, statusInfo: String) : DownloadState(progress, R.string.saving, SeaGreen, statusInfo)
    class Success(val message: String) : DownloadState(1f, R.string.success, Green, "")
    class Failure(val error: String) : DownloadState(1f, R.string.failed, Red, "")
}