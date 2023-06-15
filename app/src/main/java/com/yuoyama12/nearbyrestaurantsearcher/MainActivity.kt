package com.yuoyama12.nearbyrestaurantsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.MapsInitializer
import com.yuoyama12.nearbyrestaurantsearcher.ui.detail.DetailScreen
import com.yuoyama12.nearbyrestaurantsearcher.ui.search.SearchScreen
import com.yuoyama12.nearbyrestaurantsearcher.ui.theme.NearbyRestaurantSearcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this)

        setContent {
            val navController = rememberNavController()

            NearbyRestaurantSearcherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search.route
                    ) {
                        composable(Screen.Search.route) { backStackEntry ->
                            SearchScreen { shopId ->
                                if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED)
                                    navController.navigate(Screen.Detail.createRouteWithShopId(shopId)) {
                                        launchSingleTop = true
                                    }
                            }
                        }
                        composable(
                            route = "${Screen.Detail.route}/{$SHOP_ID}",
                            arguments = listOf(navArgument(SHOP_ID) { type = NavType.StringType })
                        ) { backStackEntry ->
                            val shopId = backStackEntry.arguments?.getString(SHOP_ID) ?: DEFAULT_SHOP_ID

                            DetailScreen(shopId = shopId)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NearbyRestaurantSearcherTheme {
        SearchScreen(onItemClicked = {  })
    }
}