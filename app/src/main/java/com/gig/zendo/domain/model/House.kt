package com.gig.zendo.domain.model

data class House(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val uid: String = "",
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

    val billingDay: Int? = -1,

    val createdAt: Long = System.currentTimeMillis()
){
    companion object {
        const val COLLECTION_NAME = "houses"
        const val FIELD_UID = "uid"
        const val FIELD_WATER_SERVICE = "waterService"
        const val FIELD_ELECTRIC_SERVICE = "electricService"
        const val FIELD_RENT_SERVICE = "rentService"
        const val FIELD_BILLING_DAY = "billingDay"
    }
}