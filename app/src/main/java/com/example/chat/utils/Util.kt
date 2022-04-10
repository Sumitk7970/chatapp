package com.example.chat.utils

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Extension function for showing a toast from a string
 */
fun Context.toast(text: String, duration:Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

/**
 * Extension function for showing a toast from a string resource id
 */
fun Context.toast(textId: Int, duration:Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(textId), duration).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertTimeInMillisToDate(timeInMillis: Long): String {
    val instant = Instant.ofEpochMilli(1575959745000L)
    // Adding the timezone information to be able to format it (change accordingly)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    return if (DateUtils.isToday(timeInMillis)) {
        DateTimeFormatter.ofPattern("h:mm a").format(date).toString()
    } else {
        DateTimeFormatter.ofPattern("dd/MM/yy").format(date).toString()
    }
}