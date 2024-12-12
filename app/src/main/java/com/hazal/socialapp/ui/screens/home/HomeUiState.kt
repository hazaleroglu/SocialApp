package com.hazal.socialapp.ui.screens.home

import com.hazal.socialapp.data.remote.model.SearchResponse

data class HomeUiState(
    val loading: Boolean = false,
    val searchResponse: SearchResponse? = null
)