package com.gig.zendo.domain.model

enum class Role { USER, BOT }

data class ChatMessage(
    val id: String           = System.currentTimeMillis().toString(),
    val role: Role,
    val text: String,
    val timestamp: Long      = System.currentTimeMillis()
)