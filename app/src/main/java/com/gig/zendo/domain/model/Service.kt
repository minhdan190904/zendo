package com.gig.zendo.domain.model

data class Service(
    val id: String = "",
    val name: String = "",
    val chargeValue: Long = 0L,
    val houseId: String = "",
    val chargeMethod: ChargeMethod = ChargeMethod.BY_CONSUMPTION
){
    companion object {
        const val COLLECTION_NAME = "services"
        const val FIELD_HOUSE_ID = "houseId"
    }
}