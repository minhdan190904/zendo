package com.gig.zendo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toMoney(): String {
    return this.toMoneyWithoutUnit() + " đ"
}

fun Long.toMoneyWithoutUnit(): String {
    return String.format(Locale.US, "%,d", this)
}

fun getToday(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
}

fun String.toDate(): Date {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this) ?: Date()
}

//get first day of this month
fun getFirstDayOfThisMonth(): String {
    return SimpleDateFormat("01/MM/yyyy", Locale.getDefault()).format(Date())
}

fun getCurrentYear(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
}

fun getCurrentMonth(): String {
    return SimpleDateFormat("MM", Locale.getDefault()).format(Date())
}