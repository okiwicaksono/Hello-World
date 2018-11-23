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
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTimeString)
    val calendar = Calendar.getInstance().apply {
        time = date
        add(Calendar.HOUR, 7)
    }
    val dateFormatGmt = SimpleDateFormat("E, dd MMM yyyy")
    return dateFormatGmt.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun parseToDesiredTime(dateString: String, timeString: String): String {
    val dateTimeString = "$dateString $timeString"
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTimeString)
    val calendar = Calendar.getInstance().apply {
        time = date
        add(Calendar.HOUR, 7)
    }
    val dateFormatGmt = SimpleDateFormat("HH.mm")
    return dateFormatGmt.format(calendar.time)
}

fun formatPlayerList(text: String, delimiters: String = "; "): String = text.split(delimiters).joinToString("\n")