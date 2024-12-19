package com.hazal.socialapp.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hazal.socialapp.data.remote.api.SortOptions
import com.hazal.socialapp.data.remote.model.Geocodes
import com.hazal.socialapp.data.remote.model.Main
import com.hazal.socialapp.data.remote.model.PlacePhotoResponseItem
import com.hazal.socialapp.data.remote.model.PlacePhotosResponse
import com.hazal.socialapp.data.remote.model.Result


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val places = uiState.searchResponse?.results
    val placePhotos = uiState.placePhotosResponse
    var userLocation by remember { mutableStateOf<LatLng?>(LatLng(40.9223982, 29.12608)) }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
                userLocation = LatLng(location.latitude, location.longitude)
                viewModel.getSearchedPlaces(
                    ll = "${userLocation?.latitude},${userLocation?.longitude}",
                    radius = 1000
                )
            }
        }
    }

    Content(places, placePhotos, { param ->
        viewModel.getSearchedPlaces(
            query = param,
            ll = "${userLocation?.latitude},${userLocation?.longitude}",
            radius = 1000
        )
    }, userLocation, {
        viewModel.getPlacePhotos(it, 3, SortOptions.POPULAR)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    places: List<Result?>?,
    placePhotos: PlacePhotosResponse?,
    onCategorySelected: (String) -> Unit,
    currentLocation: LatLng?,
    getPlacePhotos: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<Result?>(null) }
    var isSheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foursquare Map") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            // Harita bileşeni burada olacak
            if (showMenu) {
                CategoryMenu(onCategorySelected = { category ->
                    showMenu = false
                    onCategorySelected(category)
                })
            }
        }
        UserLocationMap(
            places,
            currentLocation
        ) { result ->
            result.fsqId?.let { id -> getPlacePhotos.invoke(id) }
            selectedPlace = result
            isSheetVisible = true
        }
        if (isSheetVisible) LocationDetailsBottomSheet(
            selectedPlace = selectedPlace,
            photos = placePhotos
        ) { isSheetVisible = false }
    }
}

@Composable
fun CategoryMenu(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        "ArtsAndEntertainment",
        "CommunityAndGovernment",
        "DiningAndDrinking",
        "HealthAndMedicine",
        "LandmarksAndOutdoors"
    )
    DropdownMenu(
        expanded = true,
        onDismissRequest = { /* Menü kapatılabilir */ }
    ) {
        categories.forEach { category ->
            DropdownMenuItem(
                text = { Text(text = category) },
                onClick = { onCategorySelected.invoke(category) }
            )
        }
    }
}

@Composable
fun UserLocationMap(
    places: List<Result?>?,
    currentLocation: LatLng?,
    onMarkerClick: (Result) -> Unit
) {

    GoogleMap(
        properties = MapProperties(isMyLocationEnabled = true),
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                currentLocation ?: LatLng(40.9223982, 29.12608), 15f
            )
        }
    ) {
        places?.forEach { place ->
            val latitude = place?.geocodes?.main?.latitude
            val longitude = place?.geocodes?.main?.longitude
            if (latitude != null && longitude != null) {
                Marker(
                    state = MarkerState(LatLng(latitude, longitude)),
                    title = place.name,
                    onInfoWindowClick = { onMarkerClick.invoke(place) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsBottomSheet(
    selectedPlace: Result?,
    photos: PlacePhotosResponse?,
    onDismiss: () -> Unit
) {
    val photoList = mutableListOf<PlacePhotoResponseItem?>()
    photos?.forEach { photoList.add(it) }
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        content = {
            if (selectedPlace != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyRow {
                        items(photoList) { image ->
                            if (image?.imageUrl != null){
                                val painter =
                                    rememberAsyncImagePainter(model = image)
                                Image(
                                    painter = painter,
                                    contentDescription = "Slider Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }
                    }
                    Text(text = "Place Details")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Name: ${selectedPlace.name}")
                    Text(text = "Latitude: ${selectedPlace.geocodes?.main?.latitude}")
                    Text(text = "Longitude: ${selectedPlace.geocodes?.main?.longitude}")
                }
            }
        },
        onDismissRequest = { onDismiss() }
    )
}

@Preview
@Composable
fun LocationDetailsBottomSheetPreview() {
    LocationDetailsBottomSheet(
        selectedPlace = Result(
            categories = null,
            chains = null,
            distance = null,
            geocodes = Geocodes(
                main = Main(
                    40.9223982, 29.12608
                )
            ),
            link = null,
            fsqId = "jfdklfdklfklşsşl",
            location = null,
            name = "Espresso LAB",
            relatedPlaces = null,
            timezone = null
        ), PlacePhotosResponse() ,{}
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    Content(
        places = listOf(
            Result(
                categories = null,
                chains = null,
                distance = null,
                geocodes = Geocodes(
                    main = Main(
                        40.9223982, 29.12608
                    )
                ),
                link = null,
                fsqId = "jfdklfdklfklşsşl",
                location = null,
                name = "Espresso LAB",
                relatedPlaces = null,
                timezone = null
            )
        ),
        PlacePhotosResponse(),
        {},
        LatLng(40.9223982, 29.12608),
        {}
    )
}