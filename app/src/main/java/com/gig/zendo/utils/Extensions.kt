package com.gig.zendo.utils

import java.util.Locale

fun toMoney(value: Long): String {
    return String.format(Locale.US, "%,d", value)
}