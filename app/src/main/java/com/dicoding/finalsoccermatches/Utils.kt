package com.dicoding.finalsoccermatches

import android.annotation.SuppressLint
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredDate(dateString: String, timeString: String): String {
    val dateTimeString = "$dateString $timeString"
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.getDefault()).parse(dateTimeString)
    val dateFormatGmt = SimpleDateFormat("E, dd MMM yyyy")
    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT+7")
    return dateFormatGmt.format(date)
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredTime(dateString: String, timeString: String): String {
    val dateTimeString = "$dateString $timeString"
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.getDefault()).parse(dateTimeString)
    val dateFormatGmt = SimpleDateFormat("HH.mm")
    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT+7")
    return dateFormatGmt.format(date)
}

fun formatPlayerList(text: String, delimiters: String = "; "): String = text.split(delimiters).joinToString("\n")