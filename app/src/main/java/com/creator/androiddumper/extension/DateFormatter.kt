package com.creator.androiddumper.extension

import android.content.res.Resources
import android.support.annotation.NonNull
import android.util.Log
import com.creator.androiddumper.R
import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedTime(@NonNull resources: Resources): String {
    val today: Calendar = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    val yesterday = Calendar.getInstance()
    yesterday.timeInMillis = today.timeInMillis
    yesterday.add(Calendar.DATE, -1)
    val newYearDay = Calendar.getInstance()
    newYearDay.set(today.get(Calendar.YEAR), 0, 1,
            0, 0, 0)
    val target: Calendar = Calendar.getInstance()
    target.timeInMillis = this
    return when {
        this > System.currentTimeMillis() -> target.toAllFormattedTime(resources)
        target.after(today) -> "${resources.getString(R.string.today)} ${target.toHMSFormattedTime(resources)}"
        target.after(yesterday) -> "${resources.getString(R.string.yesterday)} ${target.toHMSFormattedTime(resources)}"
        target.after(newYearDay) -> target.toNoYearFormattedTime(resources)
        else -> target.toAllFormattedTime(resources)
    }
}

fun Calendar.toAllFormattedTime(@NonNull resources: Resources): String {
    val formatter = SimpleDateFormat(resources.getString(R.string.date_format_all), Locale.getDefault())
    return formatter.format(timeInMillis)
}

fun Calendar.toNoYearFormattedTime(@NonNull resources: Resources): String {
    val formatter = SimpleDateFormat(resources.getString(R.string.date_format_no_year), Locale.getDefault())
    return formatter.format(timeInMillis)
}

fun Calendar.toHMSFormattedTime(@NonNull resources: Resources): String {
    val formatter = SimpleDateFormat(resources.getString(R.string.date_format_hms), Locale.getDefault())
    return formatter.format(timeInMillis)
}