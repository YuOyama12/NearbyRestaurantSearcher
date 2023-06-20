package com.yuoyama12.nearbyrestaurantsearcher.network.genre

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseGenresConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return ResponseGenresConverter()
    }

    companion object {
        fun create() = ResponseGenresConverterFactory()
    }
}