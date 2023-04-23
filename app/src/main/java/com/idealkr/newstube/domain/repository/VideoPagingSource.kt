package com.idealkr.newstube.domain.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.idealkr.newstube.data.api.NewsApi
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.util.Constants.PAGING_SIZE
import retrofit2.HttpException
import java.io.IOException

class VideoPagingSource(
    private val api: NewsApi,
    private val query: String,
    private val sort: String,
    private val page: Int
) : PagingSource<Int, Documents>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Documents>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Documents> {
        return try {
            val pageNumber = params.key ?: page

            val response = api.getVideoList(query, sort, pageNumber, params.loadSize)
            val endOfPaginationReached = response.body()?.meta?.isEnd ?: true

            val data = response.body()?.documents
            val prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1
            val nextKey = if (endOfPaginationReached) {
                null
            } else {
                pageNumber + (params.loadSize / PAGING_SIZE)
            }

            LoadResult.Page(
                data = data ?: arrayListOf(),
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}