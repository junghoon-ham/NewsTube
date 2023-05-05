package com.idealkr.newstube.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.idealkr.newstube.data.api.NewsApi
import com.idealkr.newstube.data.db.VideoDatabase
import com.idealkr.newstube.data.repository.VideoRepositoryImpl.PreferencesKeys.SORT_MODE
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.domain.model.VideoInfo
import com.idealkr.newstube.domain.repository.VideoPagingSource
import com.idealkr.newstube.domain.repository.VideoRepository
import com.idealkr.newstube.util.Constants.PAGING_SIZE
import com.idealkr.newstube.util.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val db: VideoDatabase,
    private val dataStore: DataStore<Preferences>
) : VideoRepository {

    override suspend fun searchVideoPaging(
        query: String,
        sort: String,
        page: Int
    ): Flow<PagingData<Documents>> {
        val pagingSourceFactory = { VideoPagingSource(api, query, sort, page) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    // Room
    override suspend fun insertBookmarkVideo(video: VideoInfo) {
        db.videoDao().insertBookmarkVideo(video)
    }

    override suspend fun deleteBookmarkVideo(video: VideoInfo) {
        db.videoDao().deleteBookmarkVideo(video)
    }

    override fun getBookmarkVideo(videoId: String): Flow<VideoInfo> {
        return db.videoDao().getBookmarkVideo(videoId)
    }

    override fun getBookmarkPagingVideos(): Flow<PagingData<VideoInfo>> {
        val pagingSourceFactory = { db.videoDao().getBookmarkPagingVideos() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    // DataStore
    private object PreferencesKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode")
    }

    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }
    }

    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value
            }
    }
}