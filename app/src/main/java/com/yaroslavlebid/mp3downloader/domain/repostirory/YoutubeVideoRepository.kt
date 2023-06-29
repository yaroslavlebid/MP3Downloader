package com.yaroslavlebid.mp3downloader.domain.repostirory

import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.util.Result

interface YoutubeVideoRepository {
    suspend fun getYoutubeVideo(url: String): Result<YouTubeVideo>
}