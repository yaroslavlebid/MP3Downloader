package com.yaroslavlebid.mp3downloader.domain.repostirory

import com.yaroslavlebid.mp3downloader.domain.state.FileDownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadFileRepository {
    fun downloadFile(url: String, id: String): Flow<FileDownloadState>

    fun cancelAllDownloads()
}