package com.yaroslavlebid.mp3downloader.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YouTubeVideo(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val viewCount: Long,
    val likeCount: Long,
    val audioUrl: String
) : Parcelable
