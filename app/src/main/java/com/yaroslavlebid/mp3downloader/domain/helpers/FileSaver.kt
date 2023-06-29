package com.yaroslavlebid.mp3downloader.domain.helpers

import android.net.Uri
import com.yaroslavlebid.mp3downloader.domain.state.FileSaveState
import java.io.File
import kotlinx.coroutines.flow.Flow

interface FileSaver {
    fun saveFileToDirectory(file: File, path: Uri): Flow<FileSaveState>
}