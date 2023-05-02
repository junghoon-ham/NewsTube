package com.idealkr.newstube.presentation.search

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
class SearchViewModel @Inject constructor(
    private val videoUseCases: VideoUseCases
) : ViewModel() {

    private val _getPagingResult = MutableStateFlow<PagingData<Documents>>(PagingData.empty())
    val getPagingResult: StateFlow<PagingData<Documents>> = _getPagingResult.asStateFlow()

    private val _filterChannels = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val filterChannels: StateFlow<ArrayList<String>> = _filterChannels

    private val _selectChannels = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val selectChannels: StateFlow<ArrayList<String>> = _selectChannels

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

    fun addChannel(channel: String) {
        val channels = ArrayList(_selectChannels.value)
        channels.add(channel)

        _selectChannels.value = channels
    }

    fun removeChannel(channel: String) {
        val channels = ArrayList(_selectChannels.value)
        channels.remove(channel)

        _selectChannels.value = channels
    }

    suspend fun removeAllChannel() {
        val channels = ArrayList(_selectChannels.value)
        channels.clear()

        _selectChannels.emit(channels)
        _filterChannels.emit(selectChannels.value)
    }

    suspend fun setFilter() {
        val channels = ArrayList(_selectChannels.value)
        _filterChannels.emit(channels)
    }

    val channelList = listOf(
        "YTN",
        "JTBC",
        "채널A",
        "연합뉴스",
        "MBN",
        "SBS",
        "KBS",
        "MBC",
        "TV조선",
        "비디오머그",
        "스브스",
        "엠빅뉴스"
    )
}