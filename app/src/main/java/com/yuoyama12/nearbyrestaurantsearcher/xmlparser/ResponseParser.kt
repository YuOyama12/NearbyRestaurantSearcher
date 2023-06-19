package com.yuoyama12.nearbyrestaurantsearcher.xmlparser

import android.util.Xml
import com.yuoyama12.nearbyrestaurantsearcher.data.*
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream

class ResponseParser {
    private var shops = Shops()

    fun parse(input: ByteArrayInputStream): Shops {
        resetShops()
        val parser = createParser(input)

        parser.nextTag()
        readResults(parser)

        return shops
    }

    private fun resetShops() {
        shops = Shops()
    }

    private fun createParser(input: ByteArrayInputStream): XmlPullParser {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(input, null)


        return parser
    }

    private fun readResults(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, null, "results")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "results_available" -> {
                    shops = shops.copy(resultsAvailableCount = readText(parser).toInt())
                }
                "results_returned" -> {
                    shops = shops.copy(returnedResultCount = readText(parser).toInt())
                }
                "shop" -> {
                    shops.list.add(readShop(parser))
                }
                else -> skip(parser)
            }
        }
    }

    private fun readShop(parser: XmlPullParser): Shop {
        var shop = Shop()
        parser.require(XmlPullParser.START_TAG, null, "shop")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "id" -> { shop = shop.copy(id = readText(parser)) }
                "name" -> { shop = shop.copy(name = readText(parser)) }
                "logo_image" -> { shop = shop.copy(logoImageUrl = readText(parser)) }
                "address" -> { shop = shop.copy(address = readText(parser)) }
                "access" -> { shop = shop.copy(access = readText(parser)) }
                "lat" -> { shop = shop.copy(latitude = readText(parser)) }
                "lng" -> { shop = shop.copy(longitude = readText(parser)) }
                "open" -> { shop = shop.copy(open = readText(parser)) }
                "close" -> { shop = shop.copy(close = readText(parser)) }
                "urls" -> { shop = shop.copy(pageUrl = readPageUrls(parser)) }
                "budget" -> { shop = shop.copy(budget = readBudget(parser)) }
                "genre" -> { shop = shop.copy(genre = readGenre(parser)) }
                "photo" -> { shop = shop.copy(photoUrls = readPhotoUrls(parser)) }
                else -> skip(parser)
            }
        }

        return shop
    }

    private fun readPageUrls(parser: XmlPullParser): String {
        var url = ""
        parser.require(XmlPullParser.START_TAG, null, "urls")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "pc" -> { url = readText(parser) }
                else -> skip(parser)
            }
        }

        return url
    }

    private fun readPhotoUrls(parser: XmlPullParser): PhotoUrls {
        var photoUrls = PhotoUrls()
        parser.require(XmlPullParser.START_TAG, null, "photo")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "pc" -> {
                    val pcPhotoUrls = readPcPhotoUrls(parser)
                    photoUrls = photoUrls.copy(
                        largeForPc = pcPhotoUrls["l"] ?: "",
                        mediumForPc = pcPhotoUrls["m"] ?: "",
                        smallForPc = pcPhotoUrls["s"] ?: "",
                    )
                }
                "mobile" -> {
                    val mobilePhotoUrls = readMobilePhotoUrls(parser)
                    photoUrls = photoUrls.copy(
                        largeForMobile = mobilePhotoUrls["l"] ?: "",
                        smallForMobile = mobilePhotoUrls["s"] ?: "",
                    )
                }
                else -> skip(parser)
            }
        }

        return photoUrls
    }

    private fun readPcPhotoUrls(parser: XmlPullParser): Map<String, String> {
        val pcPhotoUrlsMap = mutableMapOf<String, String>()
        parser.require(XmlPullParser.START_TAG, null, "pc")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "l" -> { pcPhotoUrlsMap["l"] = readText(parser) }
                "m" -> { pcPhotoUrlsMap["m"] = readText(parser) }
                "s" -> { pcPhotoUrlsMap["s"] = readText(parser) }
                else -> skip(parser)
            }
        }

        return pcPhotoUrlsMap
    }

    private fun readMobilePhotoUrls(parser: XmlPullParser): Map<String, String> {
        val mobilePhotoUrlsMap = mutableMapOf<String, String>()
        parser.require(XmlPullParser.START_TAG, null, "mobile")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "l" -> { mobilePhotoUrlsMap["l"] = readText(parser) }
                "s" -> { mobilePhotoUrlsMap["s"] = readText(parser) }
                else -> skip(parser)
            }
        }

        return mobilePhotoUrlsMap
    }

    private fun readBudget(parser: XmlPullParser): Budget {
        var budget = Budget()
        parser.require(XmlPullParser.START_TAG, null, "budget")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "code" -> { budget = budget.copy(code = readText(parser)) }
                "name" -> { budget = budget.copy(name = readText(parser)) }
                "average" -> { budget = budget.copy(average = readText(parser)) }
                else -> skip(parser)
            }
        }

        return budget
    }


    private fun readGenre(parser: XmlPullParser): Genre {
        var genre = Genre()
        parser.require(XmlPullParser.START_TAG, null, "genre")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "code" -> { genre = genre.copy(code = readText(parser)) }
                "name" -> { genre = genre.copy(name = readText(parser)) }
                "catch" -> { genre = genre.copy(catch = readText(parser)) }
                else -> skip(parser)
            }
        }

        return genre
    }
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}