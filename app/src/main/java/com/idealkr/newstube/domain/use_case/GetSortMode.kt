package com.idealkr.newstube.domain.use_case

import com.idealkr.newstube.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetSortMode(private val repository: VideoRepository) {

    suspend operator fun invoke(): Flow<String> {
        return repository.getSortMode().flowOn(Dispatchers.IO)
    }
}