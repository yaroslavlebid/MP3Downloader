package com.yaroslavlebid.mp3downloader.data.remote.model

data class YoutubeVideoDto(
    val id: String?,
    val url: String?,
    val title: String?,
    val thumbnail: String?,
    val view_count: Long?,
    val like_count: Long?,
    val audio_url: String?
)
