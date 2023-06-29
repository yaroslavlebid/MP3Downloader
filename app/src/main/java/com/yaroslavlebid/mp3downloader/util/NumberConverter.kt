package com.yaroslavlebid.mp3downloader.util

object NumberConverter {
    fun convertNumber(number: Long): String {
        val suffixes = arrayOf("", "K", "M", "B", "T")  // Define the suffixes

        var suffixIndex = 0
        var num = number.toDouble()

        while (num >= 1000 && suffixIndex < suffixes.size - 1) {
            suffixIndex++
            num /= 1000
        }

        return String.format("%.1f%s", num, suffixes[suffixIndex])
    }
}