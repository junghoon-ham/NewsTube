package com.idealkr.newstube.domain.use_case

import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBookmark(private val repository: VideoRepository) {

    suspend operator fun invoke(videoId: String): Flow<VideoInfo> {
        return repository.getBookmarkVideo(videoId).flowOn(Dispatchers.IO)
    }
}