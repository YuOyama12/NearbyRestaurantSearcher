package com.yuoyama12.nearbyrestaurantsearcher.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilledRoundCornerShapedBox(
    color: Color,
    borderColor: Color = color,
    roundedCornerPercentage: Int = 50,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(
                BorderStroke(0.5.dp, borderColor),
                RoundedCornerShape(roundedCornerPercentage)
            )
            .background(color),
        content = content
    )
}