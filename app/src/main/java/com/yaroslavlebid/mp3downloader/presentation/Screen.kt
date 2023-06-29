package com.yaroslavlebid.mp3downloader.presentation

sealed class Screen(val route: String) {
    object GetVideoInfoScreen : Screen("get_video_info_screen")
    object DownloadScreen : Screen("download_screen")
}