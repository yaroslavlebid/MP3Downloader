package com.yaroslavlebid.mp3downloader.util

object TimeConverter {
    fun convertTimeToString(timeMs: Int): String {
        val minutes = (timeMs / (1000 * 60)) % 60
        val seconds = (timeMs / 1000) % 60

        return String.format("%02d:%02d", minutes, seconds)
    }
}