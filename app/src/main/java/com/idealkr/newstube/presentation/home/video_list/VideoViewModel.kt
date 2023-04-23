package com.idealkr.newstube.presentation.home.video_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.domain.use_case.VideoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoUseCases: VideoUseCases
) : ViewModel() {

    private val _getPagingResult = MutableStateFlow<PagingData<Documents>>(PagingData.empty())
    val getPagingResult: StateFlow<PagingData<Documents>> = _getPagingResult.asStateFlow()

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
}