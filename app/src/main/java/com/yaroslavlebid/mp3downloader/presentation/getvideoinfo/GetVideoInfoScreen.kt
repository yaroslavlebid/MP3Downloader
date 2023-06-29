package com.yaroslavlebid.mp3downloader.presentation.getvideoinfo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.domain.model.YouTubeVideo
import com.yaroslavlebid.mp3downloader.ui.components.AppButton
import com.yaroslavlebid.mp3downloader.ui.components.AppTopBar
import com.yaroslavlebid.mp3downloader.ui.components.EditText
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun GetVideoInfoScreen(
    state: GetVideoInfoState,
    uiEffects: Flow<GetVideoInfoUiEffect>,
    modifier: Modifier = Modifier,
    actions: GetVideoInfoActions = GetVideoInfoActions()
) {
    val context = LocalContext.current
    val selectFolderLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (it.resultCode == Activity.RESULT_OK && uri != null) {
            actions.onFolderSelected(uri)
        }
    }

    val selectedFolder = remember(key1 = state.selectedFolderUri) {
        state.getDestinationFolderName(context)
    }

    LaunchedEffect(key1 = Unit) {
        uiEffects.collect {
            when (it) {
                is GetVideoInfoUiEffect.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is GetVideoInfoUiEffect.OnGrabbingSuccessful -> {
                    actions.onNavigateToDownloadScreen(it.video)
                }
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp)
        ) {
            AppTopBar()
            EditText(
                modifier.padding(bottom = 14.dp),
                value = state.url,
                onValueChange = {
                    actions.onUrlChanged(it)
                },
                labelText = stringResource(R.string.youtube_link_label),
                placeholderText = stringResource(R.string.enter_link_placeholder),
                enabled = !state.isGrabbing,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.url_icon),
                        contentDescription = stringResource(
                            R.string.url_icon_description
                        )
                    )
                },
                trailingIcon = {
                    if (state.url.isNotEmpty()) {
                        IconButton(onClick = { actions.onUrlChanged("") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close_icon),
                                contentDescription = stringResource(R.string.close_icon_content_description)
                            )
                        }
                    }
                },
            )
            EditText(
                modifier.padding(bottom = 24.dp),
                value = selectedFolder,
                readOnly = true,
                onFieldClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                    selectFolderLauncher.launch(intent)
                },
                labelText = stringResource(R.string.destination_folder_label),
                placeholderText = stringResource(R.string.folder_edit_text_placeholder),
                enabled = !state.isGrabbing,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.folder_icon),
                        contentDescription = stringResource(R.string.folder_icon_description),
                    )
                },
                helperText = stringResource(R.string.helper_text_folder_edit_text),
            )
            AppButton(
                text = if (state.isGrabbing) stringResource(R.string.grabbing_info_button_text) else stringResource(
                    R.string.download_button_text
                ),
                isLoading = state.isGrabbing,
                onClick = {
                    actions.onDownloadClicked()
                }
            )
        }
    }
}

data class GetVideoInfoActions(
    val onUrlChanged: (String) -> Unit = {},
    val onFolderSelected: (Uri) -> Unit = {},
    val onDownloadClicked: () -> Unit = {},
    val onNavigateToDownloadScreen: (YouTubeVideo) -> Unit = {}
)

@Preview
@Composable
fun GetVideoInfoScreenPreview() {
    MP3DownloaderTheme {
        GetVideoInfoScreen(state = GetVideoInfoState(), uiEffects = flow {})
    }
}