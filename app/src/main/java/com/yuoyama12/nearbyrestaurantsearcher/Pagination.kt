package com.yuoyama12.nearbyrestaurantsearcher

import com.yuoyama12.nearbyrestaurantsearcher.data.Shops

object Pagination {
    const val defaultPageNumber = 1
    const val perPageNumber = 10

    fun getMaxPageCountFrom(shops: Shops): Int {
        val resultCount = shops.resultsAvailableCount

        return if (resultCount % perPageNumber != 0) (resultCount / perPageNumber) + 1
        else resultCount / perPageNumber
    }

    fun getStartNumber(currentPageNumber: Int): Int =
        (currentPageNumber - 1) * perPageNumber + 1

}