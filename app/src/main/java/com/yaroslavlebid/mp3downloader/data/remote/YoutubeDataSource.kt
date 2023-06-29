package com.yaroslavlebid.mp3downloader.data.remote

import com.chaquo.python.Python
import com.google.gson.Gson
import com.yaroslavlebid.mp3downloader.data.remote.model.YoutubeVideoDto
import com.yaroslavlebid.mp3downloader.util.DispatcherProvider
import com.yaroslavlebid.mp3downloader.util.python.PythonMethods
import com.yaroslavlebid.mp3downloader.util.python.PythonModules
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface YoutubeDataSource {
    suspend fun getVideoInfo(url: String): YoutubeVideoDto
}

class YoutubeDataSourceImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val gson: Gson,
    private val python: Python
) : YoutubeDataSource {
    override suspend fun getVideoInfo(url: String): YoutubeVideoDto {
        return withContext(dispatcherProvider.io()) {
            val module = python.getModule(PythonModules.GET_VIDEO_INFO)
            val result = module.callAttr(PythonMethods.GET_VIDEO_INFO, url).toString()
            return@withContext gson.fromJson(result, YoutubeVideoDto::class.java)
        }
    }
}