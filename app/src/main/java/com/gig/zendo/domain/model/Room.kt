package com.gig.zendo.domain.model

data class Room(
    val id: String = "",
    val name: String = "",
    val houseId: String = "",
) {
    companion object {
        const val COLLECTION_NAME = "rooms"
        const val FIELD_HOUSE_ID = "houseId"
    }
}