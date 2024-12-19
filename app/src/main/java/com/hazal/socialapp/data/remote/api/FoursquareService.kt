package com.hazal.socialapp.data.remote.api

import com.hazal.socialapp.data.remote.model.PlacePhotosResponse
import com.hazal.socialapp.data.remote.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareService {

    @GET("places/search")
    suspend fun getSearchedPlaces(
        @Query("query") query: String?,
        @Query("ll") ll: String?,
        @Query("radius") radius: Int?,
        @Query("categories") categories: String?
    ): Response<SearchResponse>

    @GET("places/{fsq_id}/photos")
    suspend fun getPlacePhotos(
        @Path("fsq_id") id: String,
        @Query("limit") limit: Int?,
        @Query("sort") sort: SortOptions?
    ): Response<PlacePhotosResponse>

}

enum class SortOptions{
    POPULAR,
    NEWEST
}