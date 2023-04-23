package com.idealkr.newstube.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idealkr.newstube.domain.model.VideoInfo

@Database(
    entities = [VideoInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
}