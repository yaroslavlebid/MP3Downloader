package com.yaroslavlebid.mp3downloader.data.local.helpers

import android.net.Uri
import android.util.Log
import com.yaroslavlebid.mp3downloader.domain.helpers.FileSaver
import com.yaroslavlebid.mp3downloader.domain.state.FileSaveState
import com.yaroslavlebid.mp3downloader.util.DispatcherProvider
import com.yaroslavlebid.mp3downloader.util.FileHelper
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


private const val TAG = "FileSaverImpl"
class FileSaverImpl(
    private val fileHelper: FileHelper,
    private val dispatcherProvider: DispatcherProvider
) : FileSaver {
    override fun saveFileToDirectory(file: File, path: Uri): Flow<FileSaveState> {
        return flow {
            try {
                file.inputStream().use { inputStream ->
                    // !! - it's better to throw exception if something went wrong here
                    fileHelper.getOutputStreamForFile(path, file)!!.use { outputStream ->
                        val totalBytes = file.length()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(FileSaveState.Saving(progressBytes.toFloat() / totalBytes.toFloat()))
                        }
                    }
                }
                val isDeleted = file.delete() // delete audio from cache
                Log.d(TAG, "Clear cache: $isDeleted")
                emit(FileSaveState.Finished)
            } catch (e: Exception) {
                emit(FileSaveState.Failed(e))
            }
        }.flowOn(dispatcherProvider.io()).distinctUntilChanged()
    }
}