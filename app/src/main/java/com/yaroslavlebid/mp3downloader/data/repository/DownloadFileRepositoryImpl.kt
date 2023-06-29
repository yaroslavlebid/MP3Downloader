package com.yaroslavlebid.mp3downloader.data.repository

import com.yaroslavlebid.mp3downloader.data.remote.DownloadFileApi
import com.yaroslavlebid.mp3downloader.domain.repostirory.DownloadFileRepository
import com.yaroslavlebid.mp3downloader.domain.state.FileDownloadState
import com.yaroslavlebid.mp3downloader.util.DispatcherProvider
import com.yaroslavlebid.mp3downloader.util.FileHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DownloadFileRepositoryImpl(
    private val api: DownloadFileApi,
    private val dispatcherProvider: DispatcherProvider,
    private val fileHelper: FileHelper
) : DownloadFileRepository {
    override fun downloadFile(url: String, id: String): Flow<FileDownloadState> {
        return flow {
            val response = api.downloadFile(url)
            val destinationFile = fileHelper.getCachedFile(id)
            try {
                response.byteStream().use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        val totalBytes = response.contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(FileDownloadState.Downloading(progressBytes, totalBytes))
                        }
                    }
                }
                emit(FileDownloadState.Finished)
            } catch (e: Exception) {
                emit(FileDownloadState.Failed(e))
            }
        }
            .flowOn(dispatcherProvider.io())
            .distinctUntilChanged()
    }
}