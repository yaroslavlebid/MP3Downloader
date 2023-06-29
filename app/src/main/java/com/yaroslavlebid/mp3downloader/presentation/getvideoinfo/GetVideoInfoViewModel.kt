package com.yaroslavlebid.mp3downloader.presentation.getvideoinfo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavlebid.mp3downloader.domain.repostirory.YoutubeVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.yaroslavlebid.mp3downloader.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class GetVideoInfoViewModel @Inject constructor(private val youtubeVideoRepository: YoutubeVideoRepository) : ViewModel() {

    private val _state = MutableStateFlow(GetVideoInfoState())
    val state: StateFlow<GetVideoInfoState> get() = _state

    private val _uiEffects = Channel<GetVideoInfoUiEffect>()
    val uiEffect = _uiEffects.receiveAsFlow()

    fun updateUrl(url: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(url = url))
        }
    }

    fun updatePath(path: Uri) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(selectedFolderUri = path))
        }
    }

    fun grabVideo(url: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isGrabbing = true))
            val result = youtubeVideoRepository.getYoutubeVideo(url)
            if (result is Result.Success && result.data != null) {
                _uiEffects.send(GetVideoInfoUiEffect.OnGrabbingSuccessful(result.data))
            } else if (result.message != null){
                _uiEffects.send(GetVideoInfoUiEffect.ShowToast(result.message))
            }
            _state.emit(_state.value.copy(isGrabbing = false))
        }
    }

}