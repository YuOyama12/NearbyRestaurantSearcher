package com.yuoyama12.nearbyrestaurantsearcher.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.data.Budget
import com.yuoyama12.nearbyrestaurantsearcher.data.Genre

private val spacerModifier = Modifier.padding(vertical = 4.dp)
private val iconModifier = Modifier.padding(horizontal = 3.dp)
private val headerTextModifier = Modifier
    .padding(vertical = 10.dp)
    .border(1.dp, Color.DarkGray)

private const val GENRE_BUTTON = "genre"
private const val BUDGET_BUTTON = "budget"
@Composable
fun SearchFilterDialog(
    defaultSelectedGenreList: SnapshotStateList<Genre>,
    defaultSelectedBudgetList: SnapshotStateList<Budget>,
    genreList: List<Genre>,
    budgetList: List<Budget>,
    onDismissRequest: () -> Unit,
    onPositiveButtonClicked: (
        checkedGenres: List<Genre>,
        checkedBudgets: List<Budget>
            ) -> Unit
) {
    var selectedButton by remember { mutableStateOf(GENRE_BUTTON) }

    val checkedGenreList = remember { defaultSelectedGenreList }
    val checkedBudgetList = remember { defaultSelectedBudgetList }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(horizontal = 12.dp),
            ) {
                Column {
                    Spacer(modifier = spacerModifier)

                    Text(
                        text = stringResource(R.string.search_filter_dialog_title),
                        modifier = Modifier.alpha(DefaultAlpha),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        val genreCheckIconColor =
                            if (checkedGenreList.isEmpty()) Color.Transparent
                            else MaterialTheme.colorScheme.primary

                        val budgetCheckIconColor =
                            if (checkedBudgetList.isEmpty()) Color.Transparent
                            else MaterialTheme.colorScheme.primary

                        TextButton(
                            onClick = { selectedButton = GENRE_BUTTON },
                            enabled = selectedButton != GENRE_BUTTON
                        ) {
                            Text(text = stringResource(R.string.search_filter_dialog_genre_header))

                            Icon(
                                modifier = iconModifier,
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = genreCheckIconColor
                            )
                        }

                        TextButton(
                            onClick = { selectedButton = BUDGET_BUTTON },
                            enabled = selectedButton != BUDGET_BUTTON
                        ) {
                            Text(text = stringResource(R.string.list_item_budget_header))

                            Icon(
                                modifier = iconModifier,
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = budgetCheckIconColor
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 3.dp),
                        thickness = 2.dp
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        if (selectedButton == GENRE_BUTTON) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    modifier = headerTextModifier,
                                    text = stringResource(R.string.search_filter_dialog_genre_header),
                                )

                                Box(modifier = Modifier.weight(1f))

                                ResetButton (onClick = { checkedGenreList.clear() })
                            }

                            GridCheckBoxes(
                                items = genreList,
                                itemText = { it.name },
                                itemChecked = { checkedGenreList.contains(it) },
                                onCheckedChange = { item, checked ->
                                    if (checked) checkedGenreList.add(item)
                                    else checkedGenreList.remove(item)
                                }
                            )
                        } else {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    modifier = headerTextModifier,
                                    text = stringResource(R.string.list_item_budget_header),
                                )

                                Box(modifier = Modifier.weight(1f))

                                ResetButton (onClick = { checkedBudgetList.clear() })
                            }

                            Text(
                                modifier = Modifier.padding(all = 3.dp),
                                text = stringResource(R.string.search_filter_dialog_message_on_budget),
                                fontSize = 16.sp
                            )

                            GridCheckBoxes(
                                items = budgetList,
                                itemText = { it.name },
                                itemChecked = { checkedBudgetList.contains(it) },
                                itemEnabled = { checkedBudgetList.size < 2 || checkedBudgetList.contains(it) },
                                onCheckedChange = { item, checked ->
                                    if (checked) {
                                        if (checkedBudgetList.size >= 2) return@GridCheckBoxes

                                        checkedBudgetList.add(item)
                                    } else {
                                        checkedBudgetList.remove(item)
                                    }
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() }
                        ) {
                            Text(text = stringResource(R.string.search_filter_dialog_negative_button))
                        }
                        TextButton(
                            onClick = {
                                onPositiveButtonClicked(checkedGenreList, checkedBudgetList)
                                onDismissRequest()
                            }
                        ) {
                            Text(text = stringResource(R.string.search_filter_dialog_positive_button))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetButton(
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.search_filter_dialog_reset_button),
            fontSize = 14.sp
        )
    }


}

@Composable
private fun <E> GridCheckBoxes(
    modifier: Modifier = Modifier,
    items: List<E>,
    itemText: (item: E) -> String,
    itemChecked: (item: E) -> Boolean,
    itemEnabled: (item: E) -> Boolean = { true },
    onCheckedChange: (item: E, checked: Boolean) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3)
    ) {
        items(items) { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = itemChecked(item),
                    enabled = itemEnabled(item),
                    onCheckedChange = { checked ->
                        onCheckedChange(item, checked)
                    }
                )

                Text(
                    text = itemText(item),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun ErrorAlertDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmClicked() }
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun NetworkConnectionErrorDialog(
    onDismissRequest: () -> Unit = {  },
    onConfirmClicked: () -> Unit
) {
    ErrorAlertDialog(
        title = stringResource(R.string.network_connection_error_dialog_title),
        message = stringResource(R.string.network_connection_error_dialog_message),
        onDismissRequest = onDismissRequest,
        onConfirmClicked = onConfirmClicked
    )
}

