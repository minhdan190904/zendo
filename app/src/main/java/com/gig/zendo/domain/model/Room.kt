package com.gig.zendo.domain.model

data class Room(
    val id: String = "",
    val name: String = "",
    val houseId: String = "",
    val numberOfNotPaidInvoice: Int = 0,
    val outstandingAmount: Long = 0L,
    val empty : Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val COLLECTION_NAME = "rooms"
        const val FIELD_HOUSE_ID = "houseId"
        const val FIELD_EMPTY = "empty"
    }
}