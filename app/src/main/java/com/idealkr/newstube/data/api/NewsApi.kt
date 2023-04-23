package com.idealkr.newstube.data.api

import com.idealkr.newstube.domain.model.SearchResponse
import com.idealkr.newstube.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v2/search/image")
    suspend fun getVideoList(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<SearchResponse>

}