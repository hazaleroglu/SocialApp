package com.hazal.socialapp.data.remote.api

import com.hazal.socialapp.data.remote.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoursquareService {

    @GET("places/search")
    suspend fun getSearchedPlaces(
        @Query("query") query: String?,
        @Query("ll") ll: String?,
        @Query("radius") radius: Int?,
        @Query("categories") categories: String?
    ): Response<SearchResponse>

}