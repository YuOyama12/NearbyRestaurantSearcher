package com.yuoyama12.nearbyrestaurantsearcher.ui.search

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.nearbyrestaurantsearcher.HOT_PEPPER_API_KEY
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.data.*
import com.yuoyama12.nearbyrestaurantsearcher.network.budget.HotPepperBudgetService
import com.yuoyama12.nearbyrestaurantsearcher.network.genre.HotPepperGenreService
import com.yuoyama12.nearbyrestaurantsearcher.network.gourmet.HotPepperGourmetSearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: HotPepperGourmetSearchService,
    private val genreService: HotPepperGenreService,
    private val budgetService: HotPepperBudgetService
) : ViewModel() {
    private val _shops = MutableStateFlow(Shops())
    val shops: StateFlow<Shops> = _shops.asStateFlow()

    private var _genres = MutableStateFlow(Genres())
    val genres = _genres.asStateFlow()

    private var _budgets = MutableStateFlow(Budgets())
    val budgets = _budgets.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val currentSearchInfo = mutableMapOf<String, String>()

    private val _currentSelectedGenreList = MutableStateFlow(SnapshotStateList<Genre>())
    val currentSelectedGenreList: StateFlow<SnapshotStateList<Genre>> = _currentSelectedGenreList

    private val _currentSelectedBudgetList = MutableStateFlow(SnapshotStateList<Budget>())
    val currentSelectedBudgetList: StateFlow<SnapshotStateList<Budget>> = _currentSelectedBudgetList

    init {
        viewModelScope.launch {
            _genres.value = genreService.fetchGenres(HOT_PEPPER_API_KEY)
            _budgets.value = budgetService.fetchBudgets(HOT_PEPPER_API_KEY)
        }
    }

    fun searchShops(
        latitude: String,
        longitude: String,
        radius: RadiusForMap.Radius,
        fetchCount: String
    ) {
        resetCurrentSearchInfo()

        //検索の度に結果が１つ以上あるかどうかを確認したいため、hasResultをリセットする。。
        _shops.value = _shops.value.copy(hasResult = null)

        val range = RadiusForMap.getRangeForApi(radius)

        viewModelScope.launch {
            _isSearching.value = true

            val shops = searchService.fetchShops(
                key = HOT_PEPPER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                range = range,
                count = fetchCount,
                start = "",
                genre = convertListToQueryString(currentSelectedGenreList.value) { genre -> genre.code },
                budget = convertListToQueryString(currentSelectedBudgetList.value) { budget -> budget.code }
            )

            _isSearching.value = false
            _shops.value = shops
        }

        storeSearchInfo(latitude, longitude, range)
    }

    fun searchShopsOnPaging(
        fetchCount: String,
        fetchStartFrom: String
    ) {
        if (currentSearchInfo.isEmpty()) return

        viewModelScope.launch {
            _isSearching.value = true

            val shops = searchService.fetchShops(
                key = HOT_PEPPER_API_KEY,
                latitude = currentSearchInfo["latitude"]!!,
                longitude = currentSearchInfo["longitude"]!!,
                range = currentSearchInfo["range"]!!,
                count = fetchCount,
                start = fetchStartFrom,
                genre = convertListToQueryString(currentSelectedGenreList.value) { genre -> genre.code },
                budget = convertListToQueryString(currentSelectedBudgetList.value) { budget -> budget.code }
            )

            _isSearching.value = false
            _shops.value = shops
        }
    }

    fun searchShopsOnFilterChanged(
        fetchCount: String,
        genreList: List<Genre>,
        budgetList: List<Budget>,
        onSearchStart: () -> Unit
    ) {
        storeFilterList(genreList, budgetList)

        if (currentSearchInfo.isEmpty()) return

        onSearchStart()
        viewModelScope.launch {
            _isSearching.value = true

            val shops = searchService.fetchShops(
                key = HOT_PEPPER_API_KEY,
                latitude = currentSearchInfo["latitude"]!!,
                longitude = currentSearchInfo["longitude"]!!,
                range = currentSearchInfo["range"]!!,
                count = fetchCount,
                start = "",
                genre = convertListToQueryString(currentSelectedGenreList.value) { genre -> genre.code },
                budget = convertListToQueryString(currentSelectedBudgetList.value) { budget -> budget.code }
            )

            _isSearching.value = false
            _shops.value = shops
        }
    }

    private fun storeSearchInfo(
        latitude: String,
        longitude: String,
        range: String
    ) {
        currentSearchInfo["latitude"] = latitude
        currentSearchInfo["longitude"] = longitude
        currentSearchInfo["range"] = range
    }

    private fun resetCurrentSearchInfo() {
        currentSearchInfo.clear()
    }

    fun storeFilterList(
        genreList: List<Genre>,
        budgetList: List<Budget>
    ) {
        _currentSelectedGenreList.value = genreList.toMutableStateList()
        _currentSelectedBudgetList.value = budgetList.toMutableStateList()
    }

    private fun <E> convertListToQueryString(
        list: List<E>,
        singleQueryString:(E) -> String
    ): String {
        var result = ""

        return when (list.size) {
            0 -> result
            1 -> singleQueryString(list[0])
            else -> {
                list.forEach {
                    result += singleQueryString(it) + ","
                }
                result
            }
        }
    }

}