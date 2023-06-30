package com.yaroslavlebid.mp3downloader.util

object TimeConverter {
    fun convertTimeToString(timeMs: Int): String {
        val hours = (timeMs / (1000 * 60 * 60)) % 24
        val minutes = (timeMs / (1000 * 60)) % 60
        val seconds = (timeMs / 1000) % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}