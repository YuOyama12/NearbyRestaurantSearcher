package com.yuoyama12.nearbyrestaurantsearcher.network.genre

import com.yuoyama12.nearbyrestaurantsearcher.BuildConfig
import com.yuoyama12.nearbyrestaurantsearcher.data.Genres
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperGenreService {
    @GET("genre/v1")
    suspend fun fetchGenres(
        @Query("key") key: String = BuildConfig.API_KEY
    ): Genres
}