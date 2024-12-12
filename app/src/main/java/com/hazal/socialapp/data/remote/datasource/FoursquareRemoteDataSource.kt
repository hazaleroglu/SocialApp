package com.hazal.socialapp.data.remote.datasource

import com.hazal.socialapp.data.remote.api.FoursquareService
import javax.inject.Inject

class FoursquareRemoteDataSource @Inject constructor(
    private val foursquareService: FoursquareService
) {
    suspend fun getSearchedPlace(query: String?, ll: String?, radius: Int?, categories: String?) =
        foursquareService.getSearchedPlaces(query, ll, radius, categories)
}