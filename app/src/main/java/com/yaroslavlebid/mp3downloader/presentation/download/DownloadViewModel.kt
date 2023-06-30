package com.yaroslavlebid.mp3downloader.presentation.download

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.data.remote.exceptions.NoConnectivityException
import com.yaroslavlebid.mp3downloader.domain.helpers.AudioConverter
import com.yaroslavlebid.mp3downloader.domain.helpers.FileSaver
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.domain.repostirory.DownloadFileRepository
import com.yaroslavlebid.mp3downloader.domain.state.AudioConvertState
import com.yaroslavlebid.mp3downloader.domain.state.FileDownloadState
import com.yaroslavlebid.mp3downloader.domain.state.FileSaveState
import com.yaroslavlebid.mp3downloader.util.ByteConverter
import com.yaroslavlebid.mp3downloader.util.ResourceProvider
import com.yaroslavlebid.mp3downloader.util.StringHelper
import com.yaroslavlebid.mp3downloader.util.TimeConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "DownloadViewModel"
@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadFileRepository: DownloadFileRepository,
    private val resourceProvider: ResourceProvider,
    private val audioConverter: AudioConverter,
    private val fileSaver: FileSaver
) : ViewModel() {

    private val _state = MutableStateFlow(DownloadScreenState())
    val state: StateFlow<DownloadScreenState> get() = _state

    fun init(video: YouTubeVideo, savePath: Uri) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(item = video))
            downloadAudioFile(video, savePath)
        }
    }

    private fun downloadAudioFile(video: YouTubeVideo, destinationFolder: Uri) {
        viewModelScope.launch {
            downloadFileRepository.downloadFile(video.audioUrl, video.id).collect {
                when (it) {
                    is FileDownloadState.Downloading -> {
                        _state.emit(
                            _state.value.copy(
                                downloadState = DownloadState.Downloading(
                                    progress = it.progressBytes.toFloat() / it.totalBytes.toFloat(),
                                    statusInfo = ByteConverter.convertToInfo(
                                        it.progressBytes,
                                        it.totalBytes
                                    )
                                )
                            )
                        )
                    }
                    is FileDownloadState.Finished -> {
                        convertAudioFileToMp3(video, destinationFolder)
                    }
                    is FileDownloadState.Failed -> {
                        Log.e(TAG, "Can't download file ${it.error}")
                        val messageId = when (it.error) {
                            is NoConnectivityException -> {
                                it.error.messageId
                            }
                            is IOException -> {
                                R.string.no_enough_storage
                            }
                            else -> {
                                R.string.unknown_error
                            }
                        }
                        _state.emit(
                            _state.value.copy(
                                downloadState = DownloadState.Failure(
                                    resourceProvider.getString(
                                        messageId
                                    )
                                )
                            )
                        )
                        this.cancel()
                    }
                }
            }
        }
    }

    private fun convertAudioFileToMp3(video: YouTubeVideo, destinationFolder: Uri) {
        viewModelScope.launch {
            audioConverter.convertAudioToMp3(
                video.id,
                StringHelper.excludeSpecialCharactersSymbolsAndEmojis(video.title)
            ).collect {
                when (it) {
                    is AudioConvertState.Converting -> {
                        _state.emit(
                            _state.value.copy(
                                downloadState = DownloadState.Converting(
                                    it.currentTime.toFloat() / it.totalTime.toFloat(),
                                    "${TimeConverter.convertTimeToString(it.currentTime)} / ${
                                        TimeConverter.convertTimeToString(it.totalTime)
                                    }"
                                )
                            )
                        )
                    }
                    is AudioConvertState.Failed -> {
                        Log.e(TAG, "Can't convert file FFmpeg error: ${it.errorCode}")
                        _state.emit(
                            _state.value.copy(
                                downloadState = DownloadState.Failure(
                                    String.format(
                                        resourceProvider.getString(
                                            R.string.convert_error
                                        ), it.errorCode
                                    )
                                )
                            )
                        )
                    }
                    is AudioConvertState.Finished -> {
                        saveFileToUserSelectedFolder(it.convertedFile, destinationFolder)
                    }
                }
            }
        }
    }

    private fun saveFileToUserSelectedFolder(file: File, destinationFolder: Uri) {
        viewModelScope.launch {
            fileSaver.saveFileToDirectory(file, path = destinationFolder).collect {
                _state.emit(
                    when (it) {
                        is FileSaveState.Saving -> {
                            _state.value.copy(
                                downloadState = DownloadState.Saving(
                                    it.progress,
                                    "${(it.progress * 100).toInt()}%"
                                )
                            )
                        }
                        is FileSaveState.Failed -> {
                            Log.e(TAG, "Can't save file ${it.error}")
                            _state.value.copy(
                                downloadState = DownloadState.Failure(
                                    resourceProvider.getString(
                                        R.string.save_error
                                    )
                                )
                            )
                        }
                        is FileSaveState.Finished -> {
                            _state.value.copy(
                                downloadState = DownloadState.Success(
                                    resourceProvider.getString(
                                        R.string.success_message
                                    )
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        downloadFileRepository.cancelAllDownloads()
    }
}