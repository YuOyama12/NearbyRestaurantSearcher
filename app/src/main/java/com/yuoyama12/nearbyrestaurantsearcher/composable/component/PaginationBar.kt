package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.nearbyrestaurantsearcher.Pagination.perPageNumberList
import com.yuoyama12.nearbyrestaurantsearcher.R

private val fontSize = 12.sp
private val requiredMinWidthOfText = 32.dp
private const val TEXT_WHEN_UNABLE = "-"
@Composable
fun PaginationBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    currentPageNumber: Int,
    maxPageCount: Int,
    perPageNumber: Int,
    onForwardIconClicked: () -> Unit,
    onBackIconClicked: () -> Unit,
    onPageNumberSelected: (Int) -> Unit,
    onPerPageNumberSelected: (Int) -> Unit,
    onPageContentChanged: () -> Unit
) {
    var pageNumberOptionExpanded by remember { mutableStateOf(false)}
    var perPageOptionExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (1 < currentPageNumber) {
                    onBackIconClicked()
                    onPageContentChanged()
                }
            },
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }

        TextButton(
            onClick = { pageNumberOptionExpanded = !pageNumberOptionExpanded },
            modifier = Modifier.border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary)),
            enabled = enabled
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText),
                    text = if (enabled) currentPageNumber.toString()
                            else TEXT_WHEN_UNABLE,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = pageNumberOptionExpanded,
                onDismissRequest = { pageNumberOptionExpanded = false }
            ) {
                for (i in 1 .. maxPageCount) {
                    DropdownMenuItem(
                        text = {
                            if (enabled) { Text(text = i.toString()) }
                            else { Text(text = TEXT_WHEN_UNABLE) }
                        },
                        onClick = {
                            if (currentPageNumber != i) {
                                onPageNumberSelected(i)
                                onPageContentChanged()
                            }
                            pageNumberOptionExpanded = false
                        },
                        enabled = enabled
                    )

                    Divider()
                }
            }
        }

        Text(
            text = stringResource(R.string.max_page_count_header_for_pagination),
            modifier = Modifier.padding(start = 4.dp),
            fontSize = fontSize,
        )

        Text(
            text = if (enabled) maxPageCount.toString()
                    else TEXT_WHEN_UNABLE,
            modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText.minus(8.dp)),
            fontSize = fontSize,
            textAlign = TextAlign.End,
            maxLines = 1
        )

        IconButton(
            onClick = {
                if (currentPageNumber < maxPageCount) {
                    onForwardIconClicked()
                    onPageContentChanged()
                }
            },
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }


        Text(
            text = stringResource(R.string.per_page_header_for_pagination),
            modifier = Modifier.padding(start = 4.dp, end = 12.dp),
            fontSize = fontSize
        )

        TextButton(
            onClick = { perPageOptionExpanded = !perPageOptionExpanded },
            modifier = Modifier.border(BorderStroke(0.6.dp, MaterialTheme.colorScheme.primary))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.requiredWidthIn(min = requiredMinWidthOfText),
                    text = perPageNumber.toString(),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = perPageOptionExpanded,
                onDismissRequest = { perPageOptionExpanded = false }
            ) {
                for (i in perPageNumberList) {
                    DropdownMenuItem(
                        text = { Text(text = i.toString()) },
                        onClick = {
                            if (perPageNumber != i) {
                                onPerPageNumberSelected(i)
                                onPageContentChanged()
                            }
                            perPageOptionExpanded = false
                        }
                    )

                    Divider()
                }
            }
        }
    }

}
