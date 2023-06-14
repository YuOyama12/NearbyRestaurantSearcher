package com.yuoyama12.nearbyrestaurantsearcher.di

import com.yuoyama12.nearbyrestaurantsearcher.network.HotPepperService
import com.yuoyama12.nearbyrestaurantsearcher.xmlparser.ResponseConverterFactory
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
    fun provideHotPepperService(): HotPepperService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ResponseConverterFactory.create())
            .build()
            .create(HotPepperService::class.java)
    }
}