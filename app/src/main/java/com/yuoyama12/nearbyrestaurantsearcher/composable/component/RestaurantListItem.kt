package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledRoundCornerShapedBox
import com.yuoyama12.nearbyrestaurantsearcher.composable.NetworkImage
import com.yuoyama12.nearbyrestaurantsearcher.data.Shop

private val shopNameFontSize = 20.sp
private val subTextFontSize = 10.sp
@Composable
fun RestaurantListItem(
    modifier: Modifier = Modifier,
    shop: Shop
) {
    Row(
        modifier = modifier
            .padding(all = 5.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NetworkImage(
            url = shop.photoUrls.largeForMobile,
            modifier = modifier
                .size(80.dp)
                .shadow(3.dp),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = shop.access,
                fontSize = subTextFontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    text = shop.name,
                    fontSize = shopNameFontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                FilledRoundCornerShapedBox(
                    color = MaterialTheme.colorScheme.secondary,
                    borderColor = Color.Black
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = shop.genre.name,
                        color = Color.White,
                        fontSize = subTextFontSize
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier
                            .border(
                                BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary),
                                RectangleShape
                            )
                            .padding(horizontal = 2.dp),
                        text = stringResource(R.string.list_item_budget_header),
                        fontSize = subTextFontSize
                    )

                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = shop.budget.name,
                        fontSize = subTextFontSize
                    )
                }
            }
        }

    }
}