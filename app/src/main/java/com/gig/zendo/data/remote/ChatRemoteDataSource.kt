package com.gig.zendo.data.remote

import com.gig.zendo.domain.model.toUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.StreamResponse
import com.google.firebase.functions.functions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object ChatRemoteDataSource {

    private val functions = Firebase.functions("us-central1")
    val userCurrent = FirebaseAuth.getInstance().currentUser?.toUser()
        ?: throw IllegalStateException("User not authenticated")

    /* -------------------- helper pretty() -------------------- */
    private fun Map<*, *>?.pretty(): String {
        if (this == null) return "(no data)"
        val sb = StringBuilder()

        (this["message"]?.toString())?.let { sb.append(it) }

        fun appendList(key: String) {
            val list = this[key] as? List<*> ?: return
            if (list.isEmpty()) return
            sb.append("\n\n$key:")
            list.forEach { item ->
                sb.append("\n• ").append(item.toString())
            }
        }
        appendList("houses")
        appendList("rooms")
        appendList("tenants")
        appendList("invoices")

        return sb.toString()
    }

    /* -------------------- payload -------------------- */
    private fun makePayload(prompt: String) = mapOf(
        "text" to prompt,
        "uid"  to userCurrent.uid
    )

    /* -------------------- STREAM -------------------- */
    fun chatStream(prompt: String): Flow<String> = flow {
        val callable = functions.getHttpsCallable("aiChatGemini")
        callable.stream(makePayload(prompt))
            .asFlow()
            .collect { resp ->
                when (resp) {
                    is StreamResponse.Message -> {
                        val chunk = (resp.message.data as? Map<*, *>)?.pretty()
                            ?: resp.message.data.toString()
                        emit(chunk)
                    }
                    is StreamResponse.Result -> {
                        val final = (resp.result.data as? Map<*, *>)?.pretty()
                            ?: resp.result.data.toString()
                        emit(final)
                    }
                }
            }
    }

    /* -------------------- ONCE -------------------- */
    suspend fun chatOnce(prompt: String): String {
        val result = functions
            .getHttpsCallable("aiChatGemini")
            .call(makePayload(prompt))
            .await()
            .data as? Map<*, *>

        return result.pretty()
    }
}
