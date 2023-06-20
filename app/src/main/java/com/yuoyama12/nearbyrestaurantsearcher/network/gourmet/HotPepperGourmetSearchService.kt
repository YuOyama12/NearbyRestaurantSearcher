package com.yuoyama12.nearbyrestaurantsearcher.network.gourmet

import com.yuoyama12.nearbyrestaurantsearcher.data.Shops
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperGourmetSearchService {
    @GET("gourmet/v1")
    suspend fun fetchShops(
        @Query("key") key: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("range") range: String,
        @Query("count") count: String,
        @Query("start") start: String,
        @Query("genre") genre: String,
        @Query("budget") budget: String
    ): Shops

    @GET("gourmet/v1")
    suspend fun fetchShopById(
        @Query("key") key: String,
        @Query("id") shopId: String,
    ): Shops
}