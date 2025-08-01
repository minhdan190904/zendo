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