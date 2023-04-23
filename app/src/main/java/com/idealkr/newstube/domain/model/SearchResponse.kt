package com.idealkr.newstube.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @field:Json(name = "documents")
    val documents: List<Documents>?,
    @field:Json(name = "meta")
    val meta: Meta?
)