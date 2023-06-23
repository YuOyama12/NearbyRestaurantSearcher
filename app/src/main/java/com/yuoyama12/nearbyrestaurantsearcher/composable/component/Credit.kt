package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.nearbyrestaurantsearcher.R

private val fontSize = 14.sp
private const val URL = "http://webservice.recruit.co.jp/"
@Composable
fun Credit(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.hot_pepper_credit_header),
            fontSize = fontSize
        )

        Spacer(modifier = Modifier.padding(horizontal = 1.dp))

        Text(
            modifier = Modifier.clickable {
                uriHandler.openUri(URL)
            },
            text = stringResource(R.string.hot_pepper_credit_body),
            fontSize = fontSize,
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
    }
}