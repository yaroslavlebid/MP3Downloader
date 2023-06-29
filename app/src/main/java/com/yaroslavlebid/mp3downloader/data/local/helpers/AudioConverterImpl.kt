package com.yaroslavlebid.mp3downloader.data.local.helpers

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.yaroslavlebid.mp3downloader.domain.helpers.AudioConverter
import com.yaroslavlebid.mp3downloader.domain.state.AudioConvertState
import com.yaroslavlebid.mp3downloader.util.FileHelper
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AudioConverterImpl @Inject constructor(
    private val fileHelper: FileHelper,
    private val context: Context
) : AudioConverter {
    override fun convertAudioToMp3(cachedFileId: String, outputFileName: String): Flow<AudioConvertState> {
        return callbackFlow {
            val file = fileHelper.getCachedFile(cachedFileId)
            val convertedFile = fileHelper.getConvertedFile(outputFileName)
            val command = arrayOf<String>("-y", "-i", file.path, "-vn", convertedFile.path)

            val totalDuration = MediaPlayer.create(context, Uri.fromFile(file)).duration
            send(AudioConvertState.Converting(0, totalDuration))

            Config.enableStatisticsCallback {
                trySendBlocking(AudioConvertState.Converting(it.time, totalDuration))
            }

            FFmpeg.executeAsync(
                command
            ) { _, returnCode ->
                when (returnCode) {
                    Config.RETURN_CODE_SUCCESS -> {
                        trySendBlocking(AudioConvertState.Finished(convertedFile))
                    }
                    else -> {
                        trySendBlocking(AudioConvertState.Failed(returnCode))
                    }
                }
            }

            awaitClose {
                FFmpeg.cancel()
            }
        }
    }
}