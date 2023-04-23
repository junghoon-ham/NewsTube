package com.idealkr.newstube.domain.use_case

import androidx.paging.PagingData
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBookmarks(private val repository: VideoRepository) {

    operator fun invoke(): Flow<PagingData<VideoInfo>> {
        return repository.getBookmarkPagingVideos().flowOn(Dispatchers.IO)
    }
}