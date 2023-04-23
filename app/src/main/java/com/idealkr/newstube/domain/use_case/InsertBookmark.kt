package com.idealkr.newstube.domain.use_case

import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.repository.VideoRepository

class InsertBookmark(private val repository: VideoRepository) {

    suspend operator fun invoke(video: VideoInfo) {
        return repository.insertBookmarkVideo(video)
    }
}