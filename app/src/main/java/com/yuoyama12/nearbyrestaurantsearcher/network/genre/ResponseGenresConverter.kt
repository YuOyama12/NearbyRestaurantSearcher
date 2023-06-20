package com.yuoyama12.nearbyrestaurantsearcher.network.genre

import com.yuoyama12.nearbyrestaurantsearcher.data.Genres
import okhttp3.ResponseBody
import retrofit2.Converter

class ResponseGenresConverter : Converter<ResponseBody, Genres> {
    private val parser = ResponseGenresParser()

    override fun convert(value: ResponseBody): Genres {
        val bs = value.string().byteInputStream()
        return parser.parse(bs)
    }

}