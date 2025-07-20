package com.gig.zendo.domain.model

data class Room(
    val id: String,
    val name: String,
    val houseId: String,
    val description: String = "",
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val COLLECTION_NAME = "rooms"
    }
}