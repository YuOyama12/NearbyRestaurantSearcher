package com.yuoyama12.nearbyrestaurantsearcher.data

data class Budgets(
    val apiVersion: String = "",
    val resultsAvailableCount: Int = 0,
    val list: List<Budget> = listOf()
)
