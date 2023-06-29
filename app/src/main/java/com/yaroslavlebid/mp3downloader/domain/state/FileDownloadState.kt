package com.yaroslavlebid.mp3downloader.domain.state

sealed class FileDownloadState {
    data class Downloading(val progressBytes: Long, val totalBytes: Long) : FileDownloadState()
    object Finished : FileDownloadState()
    data class Failed(val error: Throwable? = null) : FileDownloadState()
}
