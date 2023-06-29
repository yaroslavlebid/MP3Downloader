package com.yaroslavlebid.mp3downloader.presentation.getvideoinfo

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo

data class GetVideoInfoState(
    val url: String = "",
    val selectedFolderUri: Uri? = null,
    val isGrabbing: Boolean = false
) {
    fun getDestinationFolderName(context: Context): String {
        return if (selectedFolderUri != null) {
            val docTree = DocumentFile.fromTreeUri(context, selectedFolderUri)
            docTree?.name ?: ""
        } else {
            ""
        }
    }
}

sealed interface GetVideoInfoUiEffect {
    data class OnGrabbingSuccessful(val video: YouTubeVideo) : GetVideoInfoUiEffect
    data class ShowToast(val message: String): GetVideoInfoUiEffect
}