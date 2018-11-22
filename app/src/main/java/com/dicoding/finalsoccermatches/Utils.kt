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
fun parseToDesiredDate(dateString: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    return SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(date)
}

fun formatPlayerList(text: String, delimiters: String = "; "): String = text.split(delimiters).joinToString("\n")