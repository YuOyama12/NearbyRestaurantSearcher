package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState
import com.yuoyama12.nearbyrestaurantsearcher.data.Shop

@Composable
fun RestaurantMarker(
    latLng: LatLng,
    shop: Shop,
    onFocused: (Shop, MarkerState) -> Unit
) {
    val markerState = rememberMarkerState(null, latLng)

    LaunchedEffect(latLng) { markerState.position = latLng }

    Marker(
        state = markerState,
        title = shop.name
    )

    onFocused(shop, markerState)
}