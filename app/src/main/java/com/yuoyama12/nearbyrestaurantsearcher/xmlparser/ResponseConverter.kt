package com.yuoyama12.nearbyrestaurantsearcher.xmlparser

import com.yuoyama12.nearbyrestaurantsearcher.data.Shops
import okhttp3.ResponseBody
import retrofit2.Converter

class ResponseConverter : Converter<ResponseBody, Shops> {
    private val parser = ResponseParser()

    override fun convert(value: ResponseBody): Shops {
        val bs = value.string().byteInputStream()
        return parser.parse(bs)
    }

}