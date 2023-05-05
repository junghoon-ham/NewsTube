package com.idealkr.newstube.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _channelList = MutableStateFlow<List<String>>(emptyList())
    val channelList: StateFlow<List<String>> = _channelList.asStateFlow()

    // TODO: 임시
    fun getChannelList() {
        val list = listOf(
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

        _channelList.value = list
    }
}