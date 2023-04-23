package com.idealkr.newstube.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.idealkr.newstube.domain.model.VideoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkVideo(video: VideoInfo)

    @Delete
    suspend fun deleteBookmarkVideo(video: VideoInfo)

    @Query("SELECT * FROM videos WHERE video_id = :videoId")
    fun getBookmarkVideo(videoId: String): Flow<VideoInfo>

    @Query("SELECT * FROM videos")
    fun getBookmarkPagingVideos(): PagingSource<Int, VideoInfo>
}