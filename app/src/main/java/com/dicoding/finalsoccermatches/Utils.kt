package com.dicoding.finalsoccermatches

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.CalendarContract
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

private fun parseDate(dateString: String, timeString: String): Calendar {
    val dateTimeString = "$dateString $timeString"
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTimeString)
    return Calendar.getInstance().apply {
        time = date
        add(Calendar.HOUR, 7)
    }
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredDate(dateString: String, timeString: String): String {
    val calendar = parseDate(dateString, timeString)
    val dateFormatGmt = SimpleDateFormat("E, dd MMM yyyy")
    return dateFormatGmt.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredTime(dateString: String, timeString: String): String {
    val calendar = parseDate(dateString, timeString)
    val dateFormatGmt = SimpleDateFormat("HH.mm")
    return dateFormatGmt.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredTimestamp(dateString: String, timeString: String, additionalHour: Int): Long {
    return if (additionalHour > 0)
        parseDate(dateString, timeString).apply {
            add(Calendar.HOUR, additionalHour)
        }.timeInMillis
    else parseDate(dateString, timeString).timeInMillis
}

fun createCalendarIntent(title: String, startDateMillis: Long, endDateMillis: Long, description: String): Intent =
    Intent(Intent.ACTION_EDIT).apply {
        type = "vnd.android.cursor.item/event"
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDateMillis)
        putExtra(CalendarContract.Events.ALL_DAY, false)
        putExtra(CalendarContract.Events.DESCRIPTION, description)
    }


fun formatPlayerList(text: String, delimiters: String = "; "): String = text.split(delimiters).joinToString("\n")