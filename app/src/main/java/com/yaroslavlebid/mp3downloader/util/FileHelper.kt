package com.yaroslavlebid.mp3downloader.util

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

interface FileHelper {
    fun getCachedFile(filename: String): File
    fun getConvertedFile(outputFileName: String): File
    fun getOutputStreamForFile(path: Uri, file: File): OutputStream?
}

class FileHelperImpl @Inject constructor(private val context: Context) : FileHelper{
    override fun getCachedFile(filename: String): File {
        return File(context.cacheDir, filename)
    }

    override fun getConvertedFile(outputFileName: String): File {
        val filePath: String = context.cacheDir.absolutePath + "/$outputFileName.mp3"
        return File(filePath)
    }

    override fun getOutputStreamForFile(path: Uri, file: File): OutputStream? {
        val contentResolver = context.contentResolver
        val doc = DocumentFile.fromTreeUri(context, path)
        var finalName = file.name
        var count = 1
        while (doc?.findFile(finalName) != null) {
            finalName = "${file.nameWithoutExtension} ($count).${file.extension}"
            count++
        }
        val new = doc?.createFile("mp3", finalName) ?: return null
        return contentResolver.openOutputStream(new.uri)
    }
}