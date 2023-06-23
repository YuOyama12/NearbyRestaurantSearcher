package com.yuoyama12.nearbyrestaurantsearcher

import com.yuoyama12.nearbyrestaurantsearcher.data.Shops

object Pagination {
    const val defaultPageNumber = 1
    val perPageNumberList = listOf(10, 20, 30)
    val defaultPerPageNumber = perPageNumberList[0]

    fun getMaxPageCountFrom(shops: Shops, perPageNumber: Int): Int {
        val resultCount = shops.resultsAvailableCount

        return if (resultCount % perPageNumber != 0) (resultCount / perPageNumber) + 1
        else resultCount / perPageNumber
    }

    fun getStartNumber(currentPageNumber: Int, perPageNumber: Int): Int =
        (currentPageNumber - 1) * perPageNumber + 1

}