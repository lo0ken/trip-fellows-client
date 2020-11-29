package com.tripfellows.authorization.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    private val serverDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'", Locale.ROOT)
    private val dateAndTimeFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ROOT)
    private val timeFormatter = SimpleDateFormat("hh:mm", Locale.ROOT)


    fun formatWithDateAndTime(date: String): String {
        return dateAndTimeFormatter.format(parseServerDate(date))
    }

    fun formatWithTime(date: String): String {
        return timeFormatter.format(parseServerDate(date))
    }

    fun makeServerCurrentDayWithTime(date: String): String {
        return serverDateFormatter.format(Date()) + date
    }

    private fun parseServerDate(date: String): Date {
        return serverDateFormatter.parse(date)!!
    }
}