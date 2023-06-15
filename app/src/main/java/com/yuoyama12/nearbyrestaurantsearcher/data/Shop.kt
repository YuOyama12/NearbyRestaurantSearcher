package com.yuoyama12.nearbyrestaurantsearcher.data


data class Shop(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val access: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val logoImageUrl: String = "",
    val pageUrls: String = "",
    val photoUrls: PhotoUrls = PhotoUrls(),
    val budget: Budget = Budget(),
    val genre: Genre = Genre(),
    val open: String = "",
    val close: String = ""
)
