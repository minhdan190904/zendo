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
    val note: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val deposit: Long = 0L,
    val active: Boolean = true,
    val rentService: Service = Service(
        id = "",
        name = "Rent",
        chargeValue = 0L,
        chargeMethod = ChargeMethod.FIXED
    ),
    val electricService: Service = Service(
        id = "",
        name = "Electricity",
        chargeValue = 0L,
        chargeMethod = ChargeMethod.BY_CONSUMPTION
    ),

    val waterService: Service = Service(
        id = "",
        name = "Water",
        chargeValue = 0L,
        chargeMethod = ChargeMethod.BY_CONSUMPTION
    ),
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val COLLECTION_NAME = "tenants"
        const val FIELD_ROOM_ID = "roomId"
        const val FIELD_ACTIVE = "active"
    }
}