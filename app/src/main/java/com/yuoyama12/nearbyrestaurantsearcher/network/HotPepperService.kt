package com.yuoyama12.nearbyrestaurantsearcher.network

import com.yuoyama12.nearbyrestaurantsearcher.data.Shops
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperService {
    @GET("gourmet/v1")
    suspend fun fetchShops(
        @Query("key") key: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("range") rangeNum: String
    ): Shops
}