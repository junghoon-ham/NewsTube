package com.idealkr.newstube.util

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.idealkr.newstube.domain.model.VideoInfo

fun ArrayList<VideoInfo>.toListToJsonString(): String {
    return GsonBuilder().create().toJson(
        this,
        object : TypeToken<ArrayList<VideoInfo>>() {}.type
    )
}

fun String.toJsonStringToList(): ArrayList<VideoInfo> {
    return GsonBuilder().create().fromJson(
        this,
        object : TypeToken<ArrayList<VideoInfo>>() {}.type
    )
}

fun String.toFormatDateString(): String {
    return try {
        val dateTime = this.split("T")
        "${dateTime[0]} ${dateTime[1].substring(0, 8)}"
    } catch (e: Exception) {
        ""
    }
}