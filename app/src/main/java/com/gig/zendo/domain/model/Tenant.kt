package com.gig.zendo.domain.model

data class Tenant(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val identityNumber: String = "",
    val identityCardFrontUrl: String = "",
    val identityCardBackUrl: String = "",
    val roomId: String = "",
    val numberOfOccupants: Int = 1,
    val note: String? = null,
    val rentPrice: Long = 0L,
    val startDate: String = "",
    val endDate: String = "",
) {
    companion object {
        const val COLLECTION_NAME = "tenants"
        const val FIELD_ROOM_ID = "roomId"
    }
}