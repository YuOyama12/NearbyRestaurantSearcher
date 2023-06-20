package com.yuoyama12.nearbyrestaurantsearcher.network.gourmet

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseShopsConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return ResponseShopsConverter()
    }

    companion object {
        fun create() = ResponseShopsConverterFactory()
    }
}