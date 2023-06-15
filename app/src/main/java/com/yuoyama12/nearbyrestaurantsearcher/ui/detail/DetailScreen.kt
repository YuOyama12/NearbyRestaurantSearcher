package com.yuoyama12.nearbyrestaurantsearcher.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledRoundCornerShapedBox
import com.yuoyama12.nearbyrestaurantsearcher.composable.NetworkImage

@Composable
fun DetailScreen(
    shopId: String
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val shop by viewModel.shop.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchShopById(shopId)
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize()
    ) {
        FilledRoundCornerShapedBox(
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                text = stringResource(R.string.detail_screen_shop_info_header),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            text = shop.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            NetworkImage(
                modifier = Modifier.size(320.dp),
                url = shop.photoUrls.largeForPc,
                contentScale = ContentScale.Crop
            )
        }
    }

}