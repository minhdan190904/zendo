package com.gig.zendo.domain.model

data class ServiceRecord(
    val id: String = "",
    val roomId: String = "",
    val houseId: String = "",
    val tenantId: String = "",
    val roomName: String = "",
    val tenantName: String = "",
    val electricImageUrl: String = "",
    val waterImageUrl: String = "",
    val numberElectric: Long = 0L,
    val numberWater: Long = 0L,
    val date: String = "",
    val createdAt: Long = System.currentTimeMillis(),
){
    companion object {
        const val COLLECTION_NAME = "service_records"
        const val FIELD_ROOM_ID = "roomId"
        const val FIELD_HOUSE_ID = "houseId"
        const val FIELD_TENANT_ID = "tenantId"
        const val FIELD_DATE = "date"
    }
}