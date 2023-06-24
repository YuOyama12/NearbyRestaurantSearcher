package com.yuoyama12.nearbyrestaurantsearcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Red80 = Color(0xFFF08B7C)
val LightRed80 = Color(0xFFffb4ab)
val Yellow80 = Color(0xFFffb873)

val Red40 = Color(0xFFb91e18)
val LightRed40 = Color(0xFF9c4239)
val Yellow40 = Color(0xFF8c5000)

val PurpleGrey40 = Color(0xFF625b71)
val PurpleGrey80 = Color(0xFFCCC2DC)

@Composable
fun grayColor() = if (isSystemInDarkTheme()) PurpleGrey80 else PurpleGrey40

@Composable
fun noMapBoxBackgroundColor() = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
fun textColorInFilledColoredBox() =
    if (isSystemInDarkTheme()) Color.Black else Color.White
