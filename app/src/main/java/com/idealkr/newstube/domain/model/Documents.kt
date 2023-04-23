package com.idealkr.newstube.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Documents(
    @field:Json(name = "collection")
    val collection: String?,
    @field:Json(name = "datetime")
    val datetime: String?,
    @field:Json(name = "display_sitename")
    val displaySitename: String?,
    @field:Json(name = "doc_url")
    val docUrl: String?,
    @field:Json(name = "height")
    val height: Int?,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    @field:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @field:Json(name = "width")
    val width: Int?,

    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "play_time")
    val playTime: Int?,
    @field:Json(name = "thumbnail")
    val thumbnail: String?,
    @field:Json(name = "author")
    val author: String?,
) : Parcelable