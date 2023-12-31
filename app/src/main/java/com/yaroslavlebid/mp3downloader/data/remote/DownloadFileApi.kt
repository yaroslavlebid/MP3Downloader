package com.yaroslavlebid.mp3downloader.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadFileApi {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}