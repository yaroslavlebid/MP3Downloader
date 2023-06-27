package com.yaroslavlebid.mp3downloader

import android.app.Application
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class Mp3DownloaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }
}