package com.yuoyama12.nearbyrestaurantsearcher.composable.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.RadiusForMap
import com.yuoyama12.nearbyrestaurantsearcher.ui.theme.NearbyRestaurantSearcherTheme

@Composable
fun RadiusSearcher(
    modifier: Modifier = Modifier,
    onSliderValueChanged: (radius: RadiusForMap.Radius) -> Unit,
    onSearchClicked: () -> Unit
) {
    var float by rememberSaveable {
        mutableStateOf(RadiusForMap.Radius.RADIUS_1000M.floatForSlider)
    }

    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(horizontal = 3.dp)
    ) {
        Column(
            modifier = Modifier.weight(0.8f)
        ) {
            Text(text = stringResource(R.string.search_range_label, RadiusForMap.getRadius(float).int))

            Slider(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = float,
                onValueChange = {
                    float = it
                    onSliderValueChanged(RadiusForMap.getRadius(it))
                },
                valueRange = RadiusForMap.getFloatValueRange(),
                steps = 3
            )
        }

        Button(
            onClick = { onSearchClicked() },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.2f)
                .fillMaxHeight(0.9f)
                .padding(horizontal = 2.dp),
            shape = RectangleShape
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RadiusSearcherPreview() {
    NearbyRestaurantSearcherTheme {
        RadiusSearcher(
            onSliderValueChanged = {  },
            onSearchClicked = {  }
        )
    }
}