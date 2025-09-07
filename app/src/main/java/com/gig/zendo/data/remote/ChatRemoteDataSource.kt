package com.gig.zendo.data.remote

import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await

object ChatRemoteDataSource {

    private val functions = Firebase.functions("us-central1")

    private fun Map<*, *>?.pretty(): String {
        if (this == null) return "(no data)"
        val sb = StringBuilder()

        val message = this["message"]?.toString()
        val itemType = this["itemType"]?.toString()
        val items = this["items"] as? List<*>

        if (!message.isNullOrBlank()) sb.append(message)

        if (!items.isNullOrEmpty()) {
            when (itemType) {
                "room" -> {
                    sb.append("\n")
                    items.filterIsInstance<Map<*, *>>().forEach { m ->
                        val label = m["label"]?.toString() ?: "Phòng"
                        val price = m["price"]?.toString()
                        val empty = m["empty"] == true
                        sb.append("\n• ").append(label)
                        if (empty) sb.append(" (trống)")
                        if (!price.isNullOrBlank()) sb.append(" – ").append(price)
                    }
                }
                "tenant" -> {
                    sb.append("\n")
                    items.filterIsInstance<Map<*, *>>().forEach { m ->
                        val name = m["name"]?.toString() ?: "Người thuê"
                        val room = m["room"]?.toString()
                        val occ = (m["occupants"] as? Number)?.toInt() ?: 1
                        sb.append("\n• ").append(name)
                        if (!room.isNullOrBlank()) sb.append(" – ").append(room)
                        if (occ > 1) sb.append(" (").append(occ).append(" người)")
                    }
                }
                "invoice" -> {
                    sb.append("\n")
                    items.filterIsInstance<Map<*, *>>().forEach { m ->
                        val room = m["room"]?.toString() ?: "Phòng"
                        val amt = m["amount"]?.toString() ?: ""
                        val st = m["status"]?.toString()
                        sb.append("\n• ").append(room).append(": ").append(amt)
                        if (!st.isNullOrBlank()) sb.append(" (").append(st).append(")")
                    }
                }
                "unpaid-group" -> {
                    sb.append("\n")
                    items.filterIsInstance<Map<*, *>>().forEach { m ->
                        val room = m["room"]?.toString() ?: "Phòng"
                        val count = (m["count"] as? Number)?.toInt() ?: 0
                        val total = m["total"]?.toString()
                        sb.append("\n• ").append(room)
                            .append(": ").append(count).append(" hoá đơn")
                        if (!total.isNullOrBlank()) sb.append(" – tổng ").append(total)
                    }
                }
            }
        }

        return if (sb.isNotEmpty()) sb.toString() else "(no data)"
    }

    private fun payload(prompt: String, houseId: String) = mapOf(
        "text" to prompt,
        "houseId" to houseId
    )

    /** Gọi 1 lần (non-stream), đã try/catch để không làm app văng. */
    suspend fun chatOnce(prompt: String, houseId: String): String {
        return try {
            val data = functions
                .getHttpsCallable("aiChatGemini")
                .call(payload(prompt, houseId))
                .await()
                .data as? Map<*, *>

            data.pretty()
        } catch (e: Exception) {
            "Lỗi máy chủ: ${e.message ?: "không rõ"}"
        }
    }
}
