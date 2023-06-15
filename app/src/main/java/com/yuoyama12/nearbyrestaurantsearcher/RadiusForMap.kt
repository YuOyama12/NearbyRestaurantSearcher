package com.yuoyama12.nearbyrestaurantsearcher

object RadiusForMap {
    enum class Radius(val int: Int, val floatForSlider: Float) {
        RADIUS_300M(300, 1f),
        RADIUS_500M(500, 2f),
        RADIUS_1000M(1000, 3f),
        RADIUS_2000M(2000, 4f),
        RADIUS_3000M(3000, 5f)
    }

    fun getFloatValueRange() =
        1f .. Radius.values().size.toFloat()

    fun getRadius(float: Float): Radius {
        val position = float.toInt() - 1
        return Radius.values()[position]
    }

    fun getFloatOfRadius(radius: Radius): Float {
        return when (radius) {
            Radius.RADIUS_300M -> 15.8f
            Radius.RADIUS_500M -> 15f
            Radius.RADIUS_1000M -> 14f
            Radius.RADIUS_2000M -> 13f
            Radius.RADIUS_3000M -> 12.5f
        }
    }

    fun getRangeForApi(radius: Radius): String {
        return when (radius) {
            Radius.RADIUS_300M -> "1"
            Radius.RADIUS_500M -> "2"
            Radius.RADIUS_1000M -> "3"
            Radius.RADIUS_2000M -> "4"
            Radius.RADIUS_3000M -> "5"
        }
    }
}

