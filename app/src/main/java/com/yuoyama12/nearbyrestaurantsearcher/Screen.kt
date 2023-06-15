package com.yuoyama12.nearbyrestaurantsearcher

sealed class Screen(val route: String) {
    object Search: Screen(SEARCH_SCREEN)
    object Detail: Screen(DETAIL_SCREEN) {
        fun createRouteWithShopId(id: String) =
            "${Detail.route}/$id"
    }
}
