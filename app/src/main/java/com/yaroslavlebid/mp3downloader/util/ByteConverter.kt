package com.yaroslavlebid.mp3downloader.util

private const val BYTES_IN_MB = 1000000f

object ByteConverter {
    fun convertToInfo(progressBytes: Long, totalBytes: Long): String {
        var suffix = "MB"
        var progress = progressBytes.toFloat() / BYTES_IN_MB // get MBs
        var total = totalBytes.toFloat() / BYTES_IN_MB
        if (total > BYTES_IN_MB) {
            progress /= BYTES_IN_MB // get gb
            total /= BYTES_IN_MB
            suffix = "GB"
        }
        return String.format("%.2f%s / %.2f%s", progress, suffix, total, suffix)
    }
}