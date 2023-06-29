package com.yaroslavlebid.mp3downloader.domain.state

sealed class FileSaveState {
    data class Saving(val progress: Float) : FileSaveState()
    object Finished : FileSaveState()
    data class Failed(val error: Throwable? = null) : FileSaveState()
}