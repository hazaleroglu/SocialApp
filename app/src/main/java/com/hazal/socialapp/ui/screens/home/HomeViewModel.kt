package com.hazal.socialapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazal.socialapp.data.remote.api.SortOptions
import com.hazal.socialapp.domain.repository.FoursquareRepository
import com.hazal.socialapp.internal.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: FoursquareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getSearchedPlaces(
        query: String? = null,
        ll: String? = null,
        radius: Int? = null,
        categories: String? = null
    ) {
        viewModelScope.launch {
            showLoading()
            when (val response = repo.getSearchedPlaces(query, ll, radius, categories)) {
                is NetworkResult.Success -> {
                    _uiState.update { _uiState.value.copy(searchResponse = response.data) }
                    Log.d("doga", "success: ${response.data}")
                }

                is NetworkResult.Error -> {
                    Log.d("doga", "error: ${response.message}")
                }

                is NetworkResult.Exception -> {
                    Log.d("doga", "search: ${response.e}")
                }
            }
        }
    }

    fun getPlacePhotos(id: String, limit: Int?, sort: SortOptions?) {
        viewModelScope.launch {
            showLoading()
            when (val response = repo.getPlacePhotos(id,limit, sort)) {
                is NetworkResult.Success -> {
                    _uiState.update { _uiState.value.copy(placePhotosResponse = response.data) }
                    Log.d("doga", "success: ${response.data}")
                }

                is NetworkResult.Error -> {
                    Log.d("doga", "error: ${response.message}")
                }

                is NetworkResult.Exception -> {
                    Log.d("doga", "photos: ${response.e}")
                }
            }
        }
    }

    private fun showLoading() = _uiState.update { _uiState.value.copy(loading = true) }
    private fun hideLoading() = _uiState.update { _uiState.value.copy(loading = false) }
}