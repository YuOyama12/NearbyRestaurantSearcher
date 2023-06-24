package com.yuoyama12.nearbyrestaurantsearcher.composable

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.yuoyama12.nearbyrestaurantsearcher.R
import com.yuoyama12.nearbyrestaurantsearcher.ui.theme.grayColor

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    if (url.isBlank()) {
        NoImage(modifier = modifier)
        return
    }

    var image by remember { mutableStateOf<Bitmap?>(null) }
    var drawable by remember { mutableStateOf<Drawable?>(null) }

    DisposableEffect(url) {
        val picasso = Picasso.get()

        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap
            }
        }

        picasso.load(url).error(R.drawable.baseline_error_24).into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }


    if (image != null) {
        Image(
            bitmap = image!!.asImageBitmap(),
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = null
        )
    } else if (drawable != null) {
        Image(
            painter = painterResource(R.drawable.baseline_error_24),
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = null
        )
    }
}

@Composable
fun NoImage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_image_text),
            color = Color.DarkGray
        )
    }
}

@Composable
fun NoListItemImage(
    modifier: Modifier = Modifier,
    color: Color = grayColor()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(96.dp),
                painter = painterResource(R.drawable.baseline_restaurant_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color)
            )

            Text(
                text = stringResource(R.string.no_restaurant_text),
                fontSize = 24.sp,
                color = color
            )
        }
    }
}
