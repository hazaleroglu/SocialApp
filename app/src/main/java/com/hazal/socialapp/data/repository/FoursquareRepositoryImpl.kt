package com.hazal.socialapp.data.repository

import com.hazal.socialapp.data.remote.api.SortOptions
import com.hazal.socialapp.data.remote.datasource.FoursquareRemoteDataSource
import com.hazal.socialapp.data.remote.model.PlacePhotosResponse
import com.hazal.socialapp.data.remote.model.SearchResponse
import com.hazal.socialapp.domain.repository.FoursquareRepository
import com.hazal.socialapp.internal.di.IoDispatcher
import com.hazal.socialapp.internal.util.NetworkResult
import com.hazal.socialapp.internal.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FoursquareRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: FoursquareRemoteDataSource
) : FoursquareRepository {

    override suspend fun getSearchedPlaces(
        query: String?,
        ll: String?,
        radius: Int?,
        categories: String?
    ): NetworkResult<SearchResponse> {
        return safeApiCall(defaultDispatcher = dispatcher) {
            remoteDataSource.getSearchedPlace(query, ll, radius, categories)
        }
    }

    override suspend fun getPlacePhotos(
        id: String,
        limit: Int?,
        sort: SortOptions?
    ): NetworkResult<PlacePhotosResponse> {
        return  safeApiCall(defaultDispatcher = dispatcher) {
            remoteDataSource.getPlacePhotos(id, limit, sort)
        }
    }

}