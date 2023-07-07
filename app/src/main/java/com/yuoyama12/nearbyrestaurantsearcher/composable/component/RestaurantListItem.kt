package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledGenreBox
import com.yuoyama12.nearbyrestaurantsearcher.composable.NetworkImage
import com.yuoyama12.nearbyrestaurantsearcher.data.DropdownItem
import com.yuoyama12.nearbyrestaurantsearcher.data.Shop
import com.yuoyama12.nearbyrestaurantsearcher.ui.theme.grayColor

private val shopNameFontSize = 20.sp
private val subTextFontSize = 10.sp
private const val TEXT_WHEN_NO_INFO = " ― "
@Composable
fun RestaurantListItem(
    modifier: Modifier = Modifier,
    shop: Shop,
    onItemClicked: () -> Unit,
    onMenuItemClicked: (DropdownItem) -> Unit
) {
    var expandMenu by remember { mutableStateOf(false) }
    var pressedOffset by remember { mutableStateOf(DpOffset.Zero) }
    var listItemHeight by remember { mutableStateOf(0.dp) }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(true) {
                detectTapGestures(
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    },
                    onTap = { onItemClicked() },
                    onLongPress = {
                        expandMenu = true
                        pressedOffset = DpOffset(it.x.dp, it.y.dp)
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(all = 5.dp)
                .fillMaxWidth()
                .onSizeChanged { listItemHeight = it.height.dp },
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
                    FilledGenreBox(
                        genre = shop.genre.name,
                        fontSize = subTextFontSize
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier
                                .border(
                                    BorderStroke(1.2.dp, grayColor()),
                                    RectangleShape
                                )
                                .padding(horizontal = 2.dp),
                            text = stringResource(R.string.list_item_budget_header),
                            fontSize = subTextFontSize
                        )

                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = shop.budget.name.ifEmpty { TEXT_WHEN_NO_INFO },
                            fontSize = subTextFontSize
                        )
                    }
                }
            }
        }
    }


    RestaurantListDropdownMenu(
        expanded = expandMenu,
        offset = pressedOffset.copy(y = pressedOffset.y - listItemHeight),
        onDismissRequest = { expandMenu = false },
        onMenuItemClicked = { item -> onMenuItemClicked(item) }
    )

}