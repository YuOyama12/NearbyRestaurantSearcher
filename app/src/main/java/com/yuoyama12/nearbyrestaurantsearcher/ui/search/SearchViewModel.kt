package com.yuoyama12.nearbyrestaurantsearcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.nearbyrestaurantsearcher.BuildConfig
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.network.HotPepperService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val service: HotPepperService
) : ViewModel() {
    fun fetchShops(
        latitude: String,
        longitude: String,
        radius: RadiusForMap.Radius
    ) {
        val rangeNum = when (radius) {
            RadiusForMap.Radius.RADIUS_300M -> "1"
            RadiusForMap.Radius.RADIUS_500M -> "2"
            RadiusForMap.Radius.RADIUS_1000M -> "3"
            RadiusForMap.Radius.RADIUS_2000M -> "4"
            RadiusForMap.Radius.RADIUS_3000M -> "5"
        }

        viewModelScope.launch {
            val shops = service.fetchShops(
                key = BuildConfig.API_KEY,
                latitude = latitude,
                longitude = longitude,
                rangeNum = rangeNum
            )
        }
    }
}