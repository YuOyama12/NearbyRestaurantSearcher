package com.yuoyama12.nearbyrestaurantsearcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.nearbyrestaurantsearcher.BuildConfig
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.data.Shops
import com.yuoyama12.nearbyrestaurantsearcher.network.HotPepperService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val service: HotPepperService
) : ViewModel() {
    private val _shops = MutableStateFlow(Shops())
    val shops: StateFlow<Shops> = _shops.asStateFlow()

    fun fetchShops(
        latitude: String,
        longitude: String,
        radius: RadiusForMap.Radius
    ) {
        val range = RadiusForMap.getRangeForApi(radius)

        viewModelScope.launch {
            val shops = service.fetchShops(
                key = BuildConfig.API_KEY,
                latitude = latitude,
                longitude = longitude,
                range = range
            )
            _shops.value = shops
        }
    }
}