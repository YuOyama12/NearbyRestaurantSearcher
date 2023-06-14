package com.yuoyama12.nearbyrestaurantsearcher.xmlparser

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return ResponseConverter()
    }

    companion object {
        fun create() = ResponseConverterFactory()
    }
}