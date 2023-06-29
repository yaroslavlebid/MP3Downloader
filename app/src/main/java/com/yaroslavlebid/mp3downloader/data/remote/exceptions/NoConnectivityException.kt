package com.yaroslavlebid.mp3downloader.data.remote.exceptions

import com.yaroslavlebid.mp3downloader.R
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String = "No Internet Connection"
    val messageId: Int = R.string.no_internet_connection
}