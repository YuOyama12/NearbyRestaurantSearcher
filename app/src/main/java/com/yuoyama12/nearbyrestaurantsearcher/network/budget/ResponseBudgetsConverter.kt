package com.yuoyama12.nearbyrestaurantsearcher.network.budget

import com.yuoyama12.nearbyrestaurantsearcher.data.Budgets
import okhttp3.ResponseBody
import retrofit2.Converter

class ResponseBudgetsConverter : Converter<ResponseBody, Budgets> {
    private val parser = ResponseBudgetsParser()

    override fun convert(value: ResponseBody): Budgets {
        val bs = value.string().byteInputStream()
        return parser.parse(bs)
    }

}