package com.yuoyama12.nearbyrestaurantsearcher.ui.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.composable.NoListItemImage
import com.yuoyama12.nearbyrestaurantsearcher.composable.OnExecutingIndicator
import com.yuoyama12.nearbyrestaurantsearcher.composable.component.RadiusSearcher
import com.yuoyama12.nearbyrestaurantsearcher.composable.component.RestaurantListItem

const val PERMISSION_REQUEST_CODE = 1
private val mapHeight = 265.dp
val latLngOfNullIsland = LatLng(0.0, 0.0)
@Composable
fun SearchScreen(
    onItemClicked: (shopId: String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: SearchViewModel = hiltViewModel()
    val shops by viewModel.shops.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    var currentRadius by remember { mutableStateOf(RadiusForMap.Radius.RADIUS_1000M) }
    var currentLocation by remember { mutableStateOf(latLngOfNullIsland) }
    val cameraPositionState = rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(currentLocation, RadiusForMap.getFloatOfRadius(currentRadius))
        }

    val noResultMessage = stringResource(R.string.no_result_message)

    LaunchedEffect(Unit) {
        loadCurrentLocation(context) { latAndLong -> currentLocation = latAndLong }
    }

    LaunchedEffect(currentRadius) {
        val cameraUpdateFactory =
            CameraUpdateFactory.zoomTo(RadiusForMap.getFloatOfRadius(currentRadius))

        cameraPositionState.animate(cameraUpdateFactory)
    }

    LaunchedEffect(currentLocation) {
        val cameraUpdateFactory =
            CameraUpdateFactory.newLatLngZoom(
                currentLocation,
                RadiusForMap.getFloatOfRadius(currentRadius)
            )

        cameraPositionState.animate(cameraUpdateFactory)
    }

    LaunchedEffect(shops) {

        if (shops.hasResult == false) {
            Toast.makeText(
                context,
                noResultMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column {
        RadiusSearcher (
            onSliderValueChanged = { radius -> currentRadius = radius },
            onSearchClicked = {
                loadCurrentLocation(context) { latAndLong -> currentLocation = latAndLong }

                viewModel.fetchShops(
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString(),
                    currentRadius
                )
            }
        )

        if (currentLocation != latLngOfNullIsland) {
            Box(
                modifier = Modifier
                    .height(mapHeight)
                    .fillMaxWidth()
            ) {
                GoogleMap(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(state = MarkerState(position = currentLocation))

                    Circle(
                        center = currentLocation,
                        radius = currentRadius.int.toDouble(),
                        strokeWidth = 4f
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        loadCurrentLocation(context) { latAndLong -> currentLocation = latAndLong }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .height(mapHeight)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.message_when_cannot_fetch_location))

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        loadCurrentLocation(context) { latAndLong -> currentLocation = latAndLong }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            }
        }
        
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        RectangleShape
                    )
                    .shadow(2.dp, RectangleShape)
            ) {
                Text(text = stringResource(R.string.list_header))
            }

            if (shops.list.isEmpty()) {
                NoListItemImage(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(shops.list) { shop ->
                        Column(
                            modifier = Modifier.clickable { onItemClicked(shop.id) }
                        ) {
                            RestaurantListItem(shop = shop)
                            Divider()
                        }
                    }
                }
            }
        }
    }

    if (isSearching) {
        OnExecutingIndicator(text = stringResource(R.string.on_searching_indicator_message))
    }

}

private fun loadCurrentLocation(
    context: Context,
    onTaskCompleted: (LatLng) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )

        return onTaskCompleted(latLngOfNullIsland)
    }

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener { location ->
        onTaskCompleted(LatLng(location.latitude, location.longitude))
    }
}