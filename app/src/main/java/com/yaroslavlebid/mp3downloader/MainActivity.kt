package com.yaroslavlebid.mp3downloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chaquo.python.Python
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val module = Python.getInstance().getModule("hello")
        val result = module.callAttr("say_hello").toString()
        setContent {
            MP3DownloaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(result)
                }
            }
        }
    }
}

@Composable
fun Greeting(string: String) {
    Text(text = string)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MP3DownloaderTheme {
        Greeting("Android")
    }
}