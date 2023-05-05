package com.idealkr.newstube.domain.use_case

data class VideoUseCases(
    val getVideos: GetVideos,
    val getBookmarks: GetBookmarks,
    val getBookmark: GetBookmark,
    val insertBookmark: InsertBookmark,
    val deleteBookmark: DeleteBookmark,
    val saveSortMode: SaveSortMode,
    val getSortMode: GetSortMode
)