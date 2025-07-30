package com.gig.zendo.domain.model

data class Invoice(
    val id: String = "",
    val tenant: Tenant = Tenant(),

    val date: String = "",

    val roomId: String = "",

    // Chỉ số điện nước
    val oldNumberElectric: Long = 0L,
    val newNumberElectric: Long = 0L,
    val oldNumberWater: Long = 0L,
    val newNumberWater: Long = 0L,

    //Dich vụ mac dinh

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

    // Link ảnh cũ/mới nếu có
    val oldElectricImageUrl: String = "",
    val newElectricImageUrl: String = "",
    val oldWaterImageUrl: String = "",
    val newWaterImageUrl: String = "",

    val otherServices: List<Service> = emptyList(),

    val note: String = "",

    val totalAmount: Long = 0L,

    val status: InvoiceStatus = InvoiceStatus.NOT_PAID,
    
    val createdAt: Long = System.currentTimeMillis()
){
    companion object {
        const val COLLECTION_NAME = "invoices"
        const val FIELD_ROOM_ID = "roomId"
        const val FIELD_INVOICE_STATUS = "status"
    }
}

enum class InvoiceStatus{
    PAID, NOT_PAID
}
