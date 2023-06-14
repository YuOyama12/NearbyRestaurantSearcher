package com.yuoyama12.nearbyrestaurantsearcher.data

data class Shops(
    val resultsAvailableCount: Int = 0,
    val returnedResultCount: Int = 0,
    val list: MutableList<Shop> = mutableListOf()
)