package com.idealkr.newstube.domain.use_case

import com.idealkr.newstube.domain.repository.VideoRepository

class SaveSortMode(private val repository: VideoRepository) {

    suspend operator fun invoke(mode: String) {
        return repository.saveSortMode(mode)
    }
}