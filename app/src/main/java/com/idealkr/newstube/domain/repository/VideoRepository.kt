package com.idealkr.newstube.domain.repository

import androidx.paging.PagingData
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.domain.model.VideoInfo
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    suspend fun searchVideoPaging(
        query: String,
        sort: String,
        page: Int
    ): Flow<PagingData<Documents>>

    // Room
    suspend fun insertBookmarkVideo(video: VideoInfo)

    suspend fun deleteBookmarkVideo(video: VideoInfo)

    fun getBookmarkVideo(videoId: String): Flow<VideoInfo>

    fun getBookmarkPagingVideos(): Flow<PagingData<VideoInfo>>
}