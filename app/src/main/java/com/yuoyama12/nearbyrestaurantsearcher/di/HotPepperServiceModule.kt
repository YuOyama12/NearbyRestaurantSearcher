package com.yuoyama12.nearbyrestaurantsearcher.di

import com.yuoyama12.nearbyrestaurantsearcher.network.budget.HotPepperBudgetService
import com.yuoyama12.nearbyrestaurantsearcher.network.budget.ResponseBudgetsConverterFactory
import com.yuoyama12.nearbyrestaurantsearcher.network.genre.HotPepperGenreService
import com.yuoyama12.nearbyrestaurantsearcher.network.genre.ResponseGenresConverterFactory
import com.yuoyama12.nearbyrestaurantsearcher.network.gourmet.HotPepperGourmetSearchService
import com.yuoyama12.nearbyrestaurantsearcher.network.gourmet.ResponseShopsConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

const val BASE_URL = "https://webservice.recruit.co.jp/hotpepper/"

@InstallIn(SingletonComponent::class)
@Module
object HotPepperServiceModule {
    @Singleton
    @Provides
    fun provideHotPepperGourmetSearchService(): HotPepperGourmetSearchService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ResponseShopsConverterFactory.create())
            .build()
            .create(HotPepperGourmetSearchService::class.java)
    }

    @Singleton
    @Provides
    fun provideHotPepperGenreService(): HotPepperGenreService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ResponseGenresConverterFactory.create())
            .build()
            .create(HotPepperGenreService::class.java)
    }

    @Singleton
    @Provides
    fun provideHotPepperBudgetService(): HotPepperBudgetService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ResponseBudgetsConverterFactory.create())
            .build()
            .create(HotPepperBudgetService::class.java)
    }
}