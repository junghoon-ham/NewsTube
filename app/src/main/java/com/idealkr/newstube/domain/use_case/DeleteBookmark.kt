package com.idealkr.newstube.domain.use_case

import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.repository.VideoRepository

class DeleteBookmark(private val repository: VideoRepository) {

    suspend operator fun invoke(video: VideoInfo) {
        return repository.deleteBookmarkVideo(video)
    }
}