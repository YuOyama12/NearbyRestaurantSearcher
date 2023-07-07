package com.yuoyama12.nearbyrestaurantsearcher.network.budget

import com.yuoyama12.nearbyrestaurantsearcher.BuildConfig
import com.yuoyama12.nearbyrestaurantsearcher.data.Budgets
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperBudgetService {
    @GET("budget/v1")
    suspend fun fetchBudgets(
        @Query("key") key: String = BuildConfig.API_KEY
    ): Budgets
}