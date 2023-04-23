package com.idealkr.newstube.presentation.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.use_case.VideoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchViewModel @Inject constructor(
    private val videoUseCases: VideoUseCases
) : ViewModel() {

    private val _getPagingResult = MutableStateFlow<PagingData<Documents>>(PagingData.empty())
    val getPagingResult: StateFlow<PagingData<Documents>> = _getPagingResult.asStateFlow()

    private val _isBookmarkVideo = MutableStateFlow(false)
    val isBookmarkVideo: StateFlow<Boolean> = _isBookmarkVideo

    fun getVideoList(
        query: String,
        page: Int
    ) = viewModelScope.launch {
        videoUseCases.getVideos(query, "recency", page)
            .cachedIn(viewModelScope)
            .collect {
                _getPagingResult.value = it
            }
    }

    // Room
    fun getBookmarkVideo(
        videoId: String
    ) = viewModelScope.launch {
        val video = videoUseCases.getBookmark(videoId).firstOrNull()
        _isBookmarkVideo.value = video != null
    }

    fun insertBookmarkVideo(
        video: VideoInfo
    ) = viewModelScope.launch(Dispatchers.IO) {
        videoUseCases.insertBookmark(video)
        getBookmarkVideo(video.videoId)
    }

    fun deleteBookmarkVideo(
        video: VideoInfo
    ) = viewModelScope.launch(Dispatchers.IO) {
        videoUseCases.deleteBookmark(video)
        getBookmarkVideo(video.videoId)
    }
}