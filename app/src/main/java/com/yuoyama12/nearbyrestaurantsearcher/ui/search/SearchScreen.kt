package com.yuoyama12.nearbyrestaurantsearcher.ui.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import com.yuoyama12.nearbyrestaurantsearcher.Pagination
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.composable.*
import com.yuoyama12.nearbyrestaurantsearcher.composable.component.*
import com.yuoyama12.nearbyrestaurantsearcher.data.Shop
import com.yuoyama12.nearbyrestaurantsearcher.isNetworkConnected
import kotlinx.coroutines.launch

const val PERMISSION_REQUEST_CODE = 1
private val mapHeight = 255.dp
val latLngOfNullIsland = LatLng(0.0, 0.0)
@Composable
fun SearchScreen(
    navigateToDetail: (shopId: String) -> Unit
) {
    val context = LocalContext.current
    var isInitializeSuccessful by remember { mutableStateOf(false) }

    if (!isNetworkConnected(context) && !isInitializeSuccessful) {
        NetworkConnectionErrorDialog (
            onConfirmClicked = {
                if (isNetworkConnected(context))
                    isInitializeSuccessful = true
            }
        )
        return
    } else {
        isInitializeSuccessful = true
    }

    val viewModel: SearchViewModel = hiltViewModel()
    val shops by viewModel.shops.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val genres by viewModel.genres.collectAsState()
    val budgets by viewModel.budgets.collectAsState()

    var showMap by rememberSaveable { mutableStateOf(true) }
    var currentRadius by rememberSaveable { mutableStateOf(RadiusForMap.Radius.RADIUS_1000M) }
    var currentLocation by remember { mutableStateOf(latLngOfNullIsland) }
    val cameraPositionState = rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(currentLocation, RadiusForMap.getFloatOfRadius(currentRadius))
        }
    var selectedShopOnMap by remember { mutableStateOf(Shop()) }

    var openConnectionErrorDialog by remember { mutableStateOf(false) }
    var openMiscellaneousErrorDialog by remember { mutableStateOf(false) }

    var openFilterDialog by remember { mutableStateOf(false) }
    val currentSelectedGenreList by viewModel.currentSelectedGenreList.collectAsState()
    val currentSelectedBudgetList by viewModel.currentSelectedBudgetList.collectAsState()

    var currentPageNumber by rememberSaveable { mutableStateOf(Pagination.defaultPageNumber) }
    val composableScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val noResultMessage = stringResource(R.string.no_result_message)
    val latLngErrorMessage = stringResource(R.string.lat_lng_error_message)

    LaunchedEffect(Unit) {
        loadCurrentLocation(
            context = context,
            onNetworkConnectionFailed = { openConnectionErrorDialog = true },
            onTaskCompleted = { latAndLong -> currentLocation = latAndLong },
            onTaskFailed = { openMiscellaneousErrorDialog = true }
        )
    }

    LaunchedEffect(currentRadius) {
        resetMapCameraPosition(cameraPositionState, currentLocation, currentRadius)
    }

    LaunchedEffect(currentLocation) {
        resetMapCameraPosition(cameraPositionState, currentLocation, currentRadius)
    }

    LaunchedEffect(shops) {
        selectedShopOnMap = Shop()

        if (shops.hasResult == false) {
            showToast(context, noResultMessage)
        }
    }

    Column {
        ShopSearcherWithRadius (
            onSliderValueChanged = { radius -> currentRadius = radius },
            onSearchClicked = {
                composableScope.launch {
                    resetMapCameraPosition(cameraPositionState, currentLocation, currentRadius)
                }

                currentPageNumber = Pagination.defaultPageNumber

                loadCurrentLocation(
                    context = context,
                    onNetworkConnectionFailed = { openConnectionErrorDialog = true },
                    onTaskCompleted = { latAndLong ->
                        currentLocation = latAndLong

                        viewModel.searchShops(
                            latitude = currentLocation.latitude.toString(),
                            longitude = currentLocation.longitude.toString(),
                            radius = currentRadius,
                            fetchCount = Pagination.perPageNumber.toString()
                        )
                    },
                    onTaskFailed = { openMiscellaneousErrorDialog = true }
                )
            }
        )

        AnimatedVisibility(visible = showMap) {
            if (currentLocation != latLngOfNullIsland) {
                Box(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth()
                ) {
                    GoogleMap(
                        modifier = Modifier
                            .height(mapHeight)
                            .fillMaxWidth(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = currentLocation),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )

                        Circle(
                            center = currentLocation,
                            radius = currentRadius.int.toDouble(),
                            strokeWidth = 4f,
                            strokeColor = MaterialTheme.colorScheme.secondary,
                            fillColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                        )

                        shops.list.forEach { shop ->
                            val shopLatLng: LatLng

                            try {
                                shopLatLng =
                                    LatLng(shop.latitude.toDouble(), shop.longitude.toDouble())
                            } catch (e :NumberFormatException) {
                                showToast(context, latLngErrorMessage)
                                return@GoogleMap
                            }

                            RestaurantMarker(
                                latLng = shopLatLng,
                                shop = shop
                            ) { _shop, markerState ->
                                if (_shop.id == selectedShopOnMap.id) { markerState.showInfoWindow() }
                                else { markerState.hideInfoWindow() }
                            }
                        }
                    }

                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            loadCurrentLocation(
                                context = context,
                                onNetworkConnectionFailed = { openConnectionErrorDialog = true },
                                onTaskCompleted = { latAndLong -> currentLocation = latAndLong },
                                onTaskFailed = { openMiscellaneousErrorDialog = true }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.message_when_cannot_fetch_location))

                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            loadCurrentLocation(
                                context = context,
                                onNetworkConnectionFailed = { openConnectionErrorDialog = true },
                                onTaskCompleted = { latAndLong -> currentLocation = latAndLong },
                                onTaskFailed = { openMiscellaneousErrorDialog = true }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        RectangleShape
                    )
                    .shadow(2.dp, RectangleShape)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val filterButtonColors =
                        if (currentSelectedGenreList.isNotEmpty()
                            || currentSelectedBudgetList.isNotEmpty()) {
                            ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.25f),
                            )
                        } else {
                            ButtonDefaults.textButtonColors()
                        }

                    Text(
                        text = stringResource(R.string.list_header),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(modifier = Modifier.weight(1f))

                    TextButton(
                        modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary),
                        onClick =  { openFilterDialog = true },
                        shape = RectangleShape,
                        colors = filterButtonColors
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.list_filter_button_text))

                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null
                            )
                        }
                    }

                    IconToggleButton(
                        checked = showMap,
                        onCheckedChange = { showMap = !showMap }
                    ) {
                        if (showMap) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            if (shops.list.isEmpty()) {
                NoListItemImage(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = lazyListState
                ) {
                    items(shops.list) { shop ->
                        Column {
                            RestaurantListItem(
                                shop = shop,
                                onItemClicked = {
                                    if (!showMap || (selectedShopOnMap.id == shop.id)) {
                                        navigateToDetail(shop.id)
                                        return@RestaurantListItem
                                    }

                                    try {
                                        val selectedShopLocation =
                                            LatLng(shop.latitude.toDouble(), shop.longitude.toDouble())

                                        selectedShopOnMap = shop

                                        composableScope.launch {
                                            moveMapCameraToSelectedPosition(cameraPositionState, selectedShopLocation)
                                        }
                                    } catch (e: NumberFormatException) {
                                        navigateToDetail(shop.id)
                                        return@RestaurantListItem
                                    }
                                },
                                onMenuItemClicked = { navigateToDetail(shop.id) }
                            )

                            Divider()
                        }
                    }
                }
            }

            Divider(modifier = Modifier.shadow(1.dp))

            PaginationBar(
                enabled = (Pagination.getMaxPageCountFrom(shops) != 0),
                currentPageNumber = currentPageNumber,
                maxPageCount = Pagination.getMaxPageCountFrom(shops),
                onForwardIconClicked = { currentPageNumber++ },
                onBackIconClicked = { currentPageNumber-- },
                onPageNumberSelected = { newPageNum -> currentPageNumber = newPageNum },
                onPageChanged = {
                    if (!isNetworkConnected(context)) {
                        openConnectionErrorDialog = true
                        return@PaginationBar
                    }

                    viewModel.searchShopsOnPaging(
                        fetchCount = Pagination.perPageNumber.toString(),
                        fetchStartFrom = Pagination.getStartNumber(currentPageNumber).toString()
                    )

                    composableScope.launch { lazyListState.scrollToItem(0) }
                }
            )
        }
    }

    if (openFilterDialog) {
        SearchFilterDialog(
            defaultSelectedGenreList = currentSelectedGenreList,
            defaultSelectedBudgetList = currentSelectedBudgetList,
            genreList = genres.list,
            budgetList = budgets.list,
            onDismissRequest = { openFilterDialog = false }
        ) { checkedGenres, checkedBudgets ->
            if (!isNetworkConnected(context)) {
                viewModel.storeFilterList(checkedGenres, checkedBudgets)
                openConnectionErrorDialog = true

                return@SearchFilterDialog
            }

            viewModel.searchShopsOnFilterChanged(
                fetchCount = Pagination.perPageNumber.toString(),
                genreList = checkedGenres,
                budgetList = checkedBudgets,
                onSearchStart = { currentPageNumber = Pagination.defaultPageNumber }
            )
        }
    }

    if (isSearching) {
        OnExecutingIndicator(text = stringResource(R.string.on_searching_indicator_message))
    }

    if (openMiscellaneousErrorDialog) {
        ErrorAlertDialog(
            title = stringResource(R.string.error_dialog_title),
            message = stringResource(R.string.error_dialog_message),
            onDismissRequest = { openMiscellaneousErrorDialog = false },
            onConfirmClicked = { openMiscellaneousErrorDialog = false }
        )
    }

    if (openConnectionErrorDialog) {
        NetworkConnectionErrorDialog(
            onConfirmClicked = { openConnectionErrorDialog = false }
        )
    }

}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private suspend fun resetMapCameraPosition(
    cameraPositionState: CameraPositionState,
    currentLocation: LatLng,
    currentRadius: RadiusForMap.Radius,
    ) {
    val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(currentLocation, RadiusForMap.getFloatOfRadius(currentRadius))
    cameraPositionState.animate(cameraUpdateFactory)
}

private suspend fun moveMapCameraToSelectedPosition(
    cameraPositionState: CameraPositionState,
    selectedPosition: LatLng
) {
    val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(selectedPosition, 15.5f)
    cameraPositionState.animate(cameraUpdateFactory)
}

private fun loadCurrentLocation(
    context: Context,
    onNetworkConnectionFailed: () -> Unit,
    onTaskCompleted: (LatLng) -> Unit,
    onTaskFailed: () -> Unit
) {
    if (!isNetworkConnected(context)) {
        onNetworkConnectionFailed()
        return
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )

        return onTaskCompleted(latLngOfNullIsland)
    }

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener { location ->
        onTaskCompleted(
            try {
                LatLng(location.latitude, location.longitude)
            } catch (e :java.lang.NullPointerException) {
                latLngOfNullIsland
            }
        )
    }.addOnFailureListener {
        onTaskFailed()
    }
}