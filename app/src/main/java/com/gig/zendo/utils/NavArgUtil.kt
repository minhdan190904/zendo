package com.gig.zendo.utils

import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object NavArgUtil {
    @PublishedApi
    internal val gson = Gson()

    fun <T> encode(obj: T): String {
        val json = gson.toJson(obj)
        return URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
    }

    inline fun <reified T> decode(encoded: String): T {
        val json = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
        return gson.fromJson(json, T::class.java)
    }
}
