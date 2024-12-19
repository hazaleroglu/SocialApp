package com.hazal.socialapp.domain.repository

import com.hazal.socialapp.data.remote.api.SortOptions
import com.hazal.socialapp.data.remote.model.PlacePhotosResponse
import com.hazal.socialapp.data.remote.model.SearchResponse
import com.hazal.socialapp.internal.util.NetworkResult

interface FoursquareRepository {

    suspend fun getSearchedPlaces(
        query: String? = null,
        ll: String? = null,
        radius: Int? = null,
        categories: String? = null
    ): NetworkResult<SearchResponse>

    suspend fun getPlacePhotos(
        id: String,
        limit: Int? = null,
        sort: SortOptions? = SortOptions.POPULAR
    ): NetworkResult<PlacePhotosResponse>
}