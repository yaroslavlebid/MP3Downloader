package com.yaroslavlebid.mp3downloader.domain.state

import java.io.File

sealed class AudioConvertState {
    data class Converting(val currentTime: Int, val totalTime: Int) : AudioConvertState()
    data class Finished(val convertedFile: File) : AudioConvertState()
    data class Failed(val errorCode: Int? = null) : AudioConvertState()
}