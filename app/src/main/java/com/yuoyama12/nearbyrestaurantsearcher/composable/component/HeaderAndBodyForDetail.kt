package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.nearbyrestaurantsearcher.composable.FilledRoundCornerShapedBox
import com.yuoyama12.nearbyrestaurantsearcher.ui.theme.textColorInFilledColoredBox

val detailHeaderFontSize = 20.sp
val detailBodyFontSize = 20.sp
@Composable
fun HeaderAndBodyForDetail(
    headerText: String,
    bodyText: String
) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        HeaderForDetail(text = headerText)
        BodyForDetail(text = bodyText)
    }
}

@Composable
fun HeaderForDetail(
    text: String
) {
    FilledRoundCornerShapedBox(
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 15.dp),
            text = text,
            color = textColorInFilledColoredBox(),
            fontWeight = FontWeight.Bold,
            fontSize = detailHeaderFontSize
        )
    }
}

@Composable
fun BodyForDetail(
    text: String
) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = text,
        fontSize = detailBodyFontSize
    )
}