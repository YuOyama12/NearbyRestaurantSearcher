package com.yuoyama12.nearbyrestaurantsearcher.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.nearbyrestaurantsearcher.BuildConfig
import com.yuoyama12.nearbyrestaurantsearcher.DEFAULT_SHOP_ID
import com.yuoyama12.nearbyrestaurantsearcher.data.Shop
import com.yuoyama12.nearbyrestaurantsearcher.network.HotPepperService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val service: HotPepperService
) : ViewModel() {
    private val _shop = MutableStateFlow(Shop())
    val shop: StateFlow<Shop> = _shop.asStateFlow()

    fun fetchShopById(shopId: String) {
        if (shopId != DEFAULT_SHOP_ID) {
            viewModelScope.launch {
                val shops = service.fetchShopById(
                    key = BuildConfig.API_KEY,
                    shopId = shopId
                )
                //TODO: [1]にしてどのようなエラーが起きるかを見る。
                _shop.value = shops.list[0]
            }
        } else {
            _shop.value = Shop(id = DEFAULT_SHOP_ID)
        }
    }
}