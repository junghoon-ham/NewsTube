package com.idealkr.newstube.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.use_case.VideoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val videoUseCases: VideoUseCases
) : ViewModel() {

    val bookmarkPagingVideos: StateFlow<PagingData<VideoInfo>> =
        videoUseCases.getBookmarks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
}