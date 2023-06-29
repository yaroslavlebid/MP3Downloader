package com.yaroslavlebid.mp3downloader.util

object StringHelper {
    fun excludeSpecialCharactersSymbolsAndEmojis(input: String): String {
        val pattern = "[^A-Za-z0-9 ]+".toRegex()
        return input.replace(pattern, "")
    }
}