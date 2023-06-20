package com.yuoyama12.nearbyrestaurantsearcher.network.genre

import android.util.Xml
import com.yuoyama12.nearbyrestaurantsearcher.data.*
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream

class ResponseGenresParser {
    private var genres = Genres()

    fun parse(input: ByteArrayInputStream): Genres {
        val parser = createParser(input)

        parser.nextTag()
        readResults(parser)

        return genres
    }

    private fun createParser(input: ByteArrayInputStream): XmlPullParser {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(input, null)

        return parser
    }

    private fun readResults(parser: XmlPullParser) {
        val genreList = mutableListOf<Genre>()

        parser.require(XmlPullParser.START_TAG, null, "results")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "results_available" -> {
                    genres = genres.copy(resultsAvailableCount = readText(parser).toInt())
                }
                "genre" -> { genreList.add(readGenre(parser)) }
                else -> skip(parser)
            }

            genres = genres.copy(list = genreList)
        }
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