package com.yuoyama12.nearbyrestaurantsearcher.network.budget

import android.util.Xml
import com.yuoyama12.nearbyrestaurantsearcher.data.*
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream

class ResponseBudgetsParser {
    private var budgets = Budgets()

    fun parse(input: ByteArrayInputStream): Budgets {
        val parser = createParser(input)

        parser.nextTag()
        readResults(parser)

        return budgets
    }

    private fun createParser(input: ByteArrayInputStream): XmlPullParser {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(input, null)

        return parser
    }

    private fun readResults(parser: XmlPullParser) {
        val budgetList = mutableListOf<Budget>()

        parser.require(XmlPullParser.START_TAG, null, "results")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "results_available" -> {
                    budgets = budgets.copy(resultsAvailableCount = readText(parser).toInt())
                }
                "budget" -> { budgetList.add(readBudget(parser)) }
                else -> skip(parser)
            }

            budgets = budgets.copy(list = budgetList)
        }
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
                else -> skip(parser)
            }
        }

        return budget
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