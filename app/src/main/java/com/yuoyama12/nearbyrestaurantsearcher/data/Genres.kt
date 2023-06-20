package com.yuoyama12.nearbyrestaurantsearcher.data

data class Genres(
    val apiVersion: String = "",
    val resultsAvailableCount: Int = 0,
    val list: List<Genre> = listOf()
)
