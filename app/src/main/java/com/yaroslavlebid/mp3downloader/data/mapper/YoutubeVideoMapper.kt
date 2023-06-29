package com.yaroslavlebid.mp3downloader.data.mapper

import com.yaroslavlebid.mp3downloader.data.remote.model.YoutubeVideoDto
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import javax.inject.Inject

class YoutubeVideoMapper @Inject constructor(){
    @Throws(NullPointerException::class)
    fun mapToDomainObject(dto: YoutubeVideoDto): YouTubeVideo {
        return YouTubeVideo(
            id = dto.id!!,
            title = dto.title!!,
            thumbnailUrl = dto.thumbnail!!,
            viewCount = dto.view_count!!,
            likeCount = dto.like_count!!,
            audioUrl = dto.audio_url!!
        )
    }
}