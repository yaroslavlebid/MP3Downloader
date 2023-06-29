package com.yaroslavlebid.mp3downloader.data.repository

import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.data.mapper.YoutubeVideoMapper
import com.yaroslavlebid.mp3downloader.data.remote.YoutubeDataSource
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.domain.repostirory.YoutubeVideoRepository
import com.yaroslavlebid.mp3downloader.util.ResourceProvider
import com.yaroslavlebid.mp3downloader.util.Result
import javax.inject.Inject

class YoutubeVideoRepositoryImpl @Inject constructor(
    private val youtubeDS: YoutubeDataSource,
    private val mapper: YoutubeVideoMapper,
    private val resourceProvider: ResourceProvider
) : YoutubeVideoRepository {
    override suspend fun getYoutubeVideo(url: String): Result<YouTubeVideo> {
        return try {
            val data = youtubeDS.getVideoInfo(url)
            return Result.Success(mapper.mapToDomainObject(data))
        } catch (e: Throwable) {
            val errorMessage = resourceProvider.getString(R.string.grab_video_failure)
            Result.Error(errorMessage)
        }
    }

}