package com.yaroslavlebid.mp3downloader.domain.helpers

import com.yaroslavlebid.mp3downloader.domain.state.AudioConvertState
import kotlinx.coroutines.flow.Flow

interface AudioConverter {
    fun convertAudioToMp3(cachedFileId: String, outputFileName: String): Flow<AudioConvertState>
}