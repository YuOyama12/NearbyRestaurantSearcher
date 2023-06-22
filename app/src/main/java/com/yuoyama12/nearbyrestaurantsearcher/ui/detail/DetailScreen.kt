package com.yuoyama12.nearbyrestaurantsearcher.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledGenreBox
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledRoundCornerShapedBox
import com.yuoyama12.nearbyrestaurantsearcher.composable.NetworkConnectionErrorDialog
import com.yuoyama12.nearbyrestaurantsearcher.composable.NetworkImage
import com.yuoyama12.nearbyrestaurantsearcher.composable.component.*
import com.yuoyama12.nearbyrestaurantsearcher.isNetworkConnected

private val spacerModifier = Modifier.padding(vertical = 3.dp)
@Composable
fun DetailScreen(
    shopId: String,
    onConnectionFailed: () -> Unit
) {
    val context = LocalContext.current
    var isInitializeSuccessful by remember { mutableStateOf(false) }

    if (!isNetworkConnected(context) && !isInitializeSuccessful) {
        NetworkConnectionErrorDialog (
            onConfirmClicked = {
                if (isNetworkConnected(context)) {
                    isInitializeSuccessful = true
                } else {
                    onConnectionFailed()
                }
            }
        )
        return
    } else {
        isInitializeSuccessful = true
    }

    val uriHandler = LocalUriHandler.current

    val viewModel: DetailViewModel = hiltViewModel()
    val shop by viewModel.shop.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchShopById(shopId)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = spacerModifier)

        FilledRoundCornerShapedBox(
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                text = stringResource(R.string.detail_screen_shop_info_header),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = detailHeaderFontSize
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

        Row(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledGenreBox(
                genre = shop.genre.name,
                fontSize = detailHeaderFontSize
            )
        }

        Divider(
            modifier = Modifier.padding(all = 6.dp),
            thickness = 2.dp
        )
        
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(3.dp)
                .fillMaxWidth(),
            onClick = { uriHandler.openUri(shop.pageUrl) },
            enabled = (shop.pageUrl.isNotEmpty()),
            shape = RoundedCornerShape(5),
            elevation =
            buttonElevation(
                defaultElevation = 9.dp,
                pressedElevation = 2.dp,
                focusedElevation = 5.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(
                text = stringResource(R.string.move_to_hot_pepper_button_text),
                fontSize = detailHeaderFontSize
            )
        }

        HeaderAndBodyForDetail(
            headerText = stringResource(R.string.detail_screen_shop_address_header),
            bodyText = shop.address
        )

        HeaderAndBodyForDetail(
            headerText = stringResource(R.string.detail_screen_shop_access_header),
            bodyText = shop.access
        )

        HeaderForDetail(text = stringResource(R.string.detail_screen_shop_open_and_close_header))

        Column(modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)) {
            Row {
                Text(
                    modifier = Modifier.border(1.dp, Color.DarkGray),
                    text = stringResource(R.string.detail_screen_shop_open_header),
                    fontSize = detailBodyFontSize,
                    fontWeight = FontWeight.Bold
                )

                BodyForDetail(text = shop.open)
            }

            Spacer(modifier = spacerModifier)

            Row {
                Text(
                    modifier = Modifier.border(1.dp, Color.DarkGray),
                    text = stringResource(R.string.detail_screen_shop_close_header),
                    fontSize = detailBodyFontSize,
                    fontWeight = FontWeight.Bold
                )

                BodyForDetail(text = shop.close)
            }

            Spacer(modifier = spacerModifier)
        }

        HeaderAndBodyForDetail(
            headerText = stringResource(R.string.list_item_budget_header),
            bodyText = shop.budget.name
        )
    }

}