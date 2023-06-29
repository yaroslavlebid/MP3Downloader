package com.yaroslavlebid.mp3downloader.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.presentation.download.DownloadScreen
import com.yaroslavlebid.mp3downloader.presentation.download.DownloadViewModel
import com.yaroslavlebid.mp3downloader.presentation.getvideoinfo.GetVideoInfoActions
import com.yaroslavlebid.mp3downloader.presentation.getvideoinfo.GetVideoInfoScreen
import com.yaroslavlebid.mp3downloader.presentation.getvideoinfo.GetVideoInfoViewModel
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme
import com.yaroslavlebid.mp3downloader.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MP3DownloaderTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.GetVideoInfoScreen.route
                ) {
                    composable(Screen.GetVideoInfoScreen.route) {
                        val viewModel = hiltViewModel<GetVideoInfoViewModel>()
                        val state = viewModel.state.collectAsState()
                        GetVideoInfoScreen(
                            state = state.value,
                            uiEffects = viewModel.uiEffect,
                            actions = GetVideoInfoActions(
                                onUrlChanged = {
                                    viewModel.updateUrl(it)
                                },
                                onFolderSelected = {
                                    viewModel.updatePath(it)
                                },
                                onDownloadClicked = {
                                    viewModel.grabVideo(state.value.url)
                                },
                                onNavigateToDownloadScreen = {
                                    navController.navigate(Screen.DownloadScreen.route, bundleOf(
                                        VIDEO_ARG to it,
                                        SAVE_PATH_ARG to state.value.selectedFolderUri
                                    ))
                                }
                            )
                        )
                    }
                    composable(Screen.DownloadScreen.route) {
                        val youtubeVideo = it.arguments?.getParcelable<YouTubeVideo>(VIDEO_ARG)
                        val savePath = it.arguments?.getParcelable<Uri>(SAVE_PATH_ARG)
                        if (youtubeVideo == null || savePath == null) {
                            navController.navigate(Screen.GetVideoInfoScreen.route)
                            return@composable
                        }

                        val viewModel = hiltViewModel<DownloadViewModel>()
                        val state = viewModel.state.collectAsState()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.init(youtubeVideo, savePath)
                        }

                        DownloadScreen(
                            state = state.value,
                            onLoadNext = {
                                navController.navigate(Screen.GetVideoInfoScreen.route)
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val VIDEO_ARG = "video"
        const val SAVE_PATH_ARG = "path"
    }
}