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

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val currentSearchInfo = mutableMapOf<String, String>()

    fun searchShops(
        latitude: String,
        longitude: String,
        radius: RadiusForMap.Radius,
        fetchCount: String,
        fetchStartFrom: String
    ) {
        resetCurrentSearchInfo()

        //検索の度に結果が１つ以上あるかどうかを確認したいため、hasResultをリセットする。。
        _shops.value = _shops.value.copy(hasResult = null)

        val range = RadiusForMap.getRangeForApi(radius)

        viewModelScope.launch {
            _isSearching.value = true

            val shops = service.fetchShops(
                key = BuildConfig.API_KEY,
                latitude = latitude,
                longitude = longitude,
                range = range,
                count = fetchCount,
                start = fetchStartFrom
            )

            _isSearching.value = false
            _shops.value = shops
        }

        storeSearchInfo(latitude, longitude, range)
    }

    fun searchShopsOnPaging(
        fetchCount: String,
        fetchStartFrom: String
    ) {
        if (currentSearchInfo.isEmpty()) return

        viewModelScope.launch {
            _isSearching.value = true

            val shops = service.fetchShops(
                key = BuildConfig.API_KEY,
                latitude = currentSearchInfo["latitude"]!!,
                longitude = currentSearchInfo["longitude"]!!,
                range = currentSearchInfo["range"]!!,
                count = fetchCount,
                start = fetchStartFrom
            )

            _isSearching.value = false
            _shops.value = shops
        }
    }

    private fun storeSearchInfo(
        latitude: String,
        longitude: String,
        range: String
    ) {
        currentSearchInfo["latitude"] = latitude
        currentSearchInfo["longitude"] = longitude
        currentSearchInfo["range"] = range
    }

    private fun resetCurrentSearchInfo() {
        currentSearchInfo.clear()
    }
}