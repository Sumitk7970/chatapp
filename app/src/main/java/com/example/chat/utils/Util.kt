package com.example.chat.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.Patterns
import android.widget.Toast
import com.example.chat.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Checks whether name and email are valid or not and shows toast accordingly
 */
fun isValidEmailAndPassword(context: Context, email: String, password: String) = when {
    email.isEmpty() -> {
        context.toast(R.string.enter_your_email)
        false
    }
    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
        context.toast(R.string.email_not_valid)
        false
    }
    password.isEmpty() -> {
        context.toast(R.string.enter_your_password)
        false
    }
    else -> true
}

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

/**
 * @return the date in string format
 */
fun Long.toDate(): String {
    val date = Date(this)
    return  if (DateUtils.isToday(this)) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(date)
    } else {
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        sdf.format(date)
    }
}