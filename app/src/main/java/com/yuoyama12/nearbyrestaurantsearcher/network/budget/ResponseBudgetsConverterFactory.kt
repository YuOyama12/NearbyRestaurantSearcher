package com.yuoyama12.nearbyrestaurantsearcher.network.budget

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseBudgetsConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return ResponseBudgetsConverter()
    }

    companion object {
        fun create() = ResponseBudgetsConverterFactory()
    }
}