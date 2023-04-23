package com.idealkr.newstube.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "videos")
data class VideoInfo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "video_id")
    val videoId: String,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    val title: String,
    val channel: String,
    val thumbnail: String
) : Parcelable
