package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.data.DropdownItem

@Composable
fun RestaurantListDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    onDismissRequest: () -> Unit,
    onMenuItemClicked: (DropdownItem) -> Unit
) {
    val menuList = listOf(
        DropdownItem(stringResource(R.string.list_item_menu_navigate_to_detail))
    )

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        menuList.forEach { menu ->
            DropdownMenuItem(
                text = { Text(text = menu.title) },
                onClick = {
                    onMenuItemClicked(menu)
                    onDismissRequest()
                }
            )
        }
    }
}