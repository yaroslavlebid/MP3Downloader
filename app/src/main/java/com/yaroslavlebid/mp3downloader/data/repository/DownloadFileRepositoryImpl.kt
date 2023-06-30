package com.yaroslavlebid.mp3downloader.data.repository

import com.tanodxyz.gdownload.DownloadInfo
import com.tanodxyz.gdownload.DownloadManager
import com.tanodxyz.gdownload.DownloadProgressListener
import com.tanodxyz.gdownload.NetworkType
import com.yaroslavlebid.mp3downloader.domain.repostirory.DownloadFileRepository
import com.yaroslavlebid.mp3downloader.domain.state.FileDownloadState
import com.yaroslavlebid.mp3downloader.util.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class DownloadFileRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val downloadManager: DownloadManager
) : DownloadFileRepository {
    override fun downloadFile(url: String, id: String): Flow<FileDownloadState> {
        return callbackFlow {
            downloadManager.download(
                url,
                id,
                NetworkType.ALL,
                object : DownloadProgressListener {
                    override fun onConnectionEstablished(downloadInfo: DownloadInfo?) {
                        super.onConnectionEstablished(downloadInfo)
                        trySendBlocking(
                            FileDownloadState.Downloading(
                                downloadInfo?.downloadedContentLengthBytes ?: 0L,
                                downloadInfo?.contentLengthBytes ?: 0L
                            )
                        )
                    }

                    override fun onDownloadFailed(downloadInfo: DownloadInfo, ex: String) {
                        super.onDownloadFailed(downloadInfo, ex)
                        trySendBlocking(FileDownloadState.Failed(Exception(ex)))
                    }

                    override fun onDownloadProgress(downloadInfo: DownloadInfo?) {
                        super.onDownloadProgress(downloadInfo)
                        trySendBlocking(
                            FileDownloadState.Downloading(
                                downloadInfo?.downloadedContentLengthBytes ?: 0L,
                                downloadInfo?.contentLengthBytes ?: 0L
                            )
                        )
                    }

                    override fun onDownloadSuccess(downloadInfo: DownloadInfo) {
                        super.onDownloadSuccess(downloadInfo)
                        trySendBlocking(FileDownloadState.Finished)
                    }
                })
            awaitClose {
                downloadManager.shutDown(null)
            }
        }.flowOn(dispatcherProvider.io())
    }

    override fun cancelAllDownloads() {
        downloadManager.shutDown(null)
    }
}