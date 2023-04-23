package com.idealkr.newstube.domain.use_case

import androidx.paging.PagingData
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetVideos(private val repository: VideoRepository) {

    suspend operator fun invoke(
        query: String,
        sort: String,
        page: Int
    ): Flow<PagingData<Documents>> {
        return repository.searchVideoPaging(query, sort, page).flowOn(Dispatchers.Default)
    }
}