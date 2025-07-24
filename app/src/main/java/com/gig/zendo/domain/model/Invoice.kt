package com.gig.zendo.domain.model

data class Invoice(
    val id: String = "",
    val tenantId: String = "",

    val date: String = "",

    // Chỉ số điện nước
    val oldElectric: Int = 0,
    val newElectric: Int = 0,
    val oldWater: Int = 0,
    val newWater: Int = 0,

    // Giá điện nước (nếu linh động từng hóa đơn)
    val electricPrice: Int = 0,
    val waterPrice: Int = 0,

    // Link ảnh cũ/mới nếu có
    val oldElectricImageUrl: String? = null,
    val newElectricImageUrl: String? = null,
    val oldWaterImageUrl: String? = null,
    val newWaterImageUrl: String? = null,

    // Tiền phòng và dịch vụ khác
    val roomPrice: Int = 0,
    val otherServicePrice: Int = 0,

    // Ghi chú nếu có
    val note: String? = null,

    // Tổng tiền hóa đơn (có thể tính lại khi lấy)
    val totalAmount: Int = 0,

    val status: InvoiceStatus = InvoiceStatus.NOT_PAID,

    val createdAt: Long = System.currentTimeMillis()
)

enum class InvoiceStatus{
    PAID, NOT_PAID
}
