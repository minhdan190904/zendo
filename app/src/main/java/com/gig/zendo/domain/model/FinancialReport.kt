package com.gig.zendo.domain.model

data class FinancialReport(
    val month: Int = 0,
    val year: Int = 0,
    val totalRooms: Int = 0,
    val vacantRooms: Int = 0,
    val rentedRooms: Int = 0,
    val roomRevenue: Long = 0L,
    val electricityRevenue: Long = 0L,
    val waterRevenue: Long = 0L,
    val otherRevenue: Long = 0L,
    val totalRevenue: Long = 0L,
    val electricityCost: Long = 0L,
    val waterCost: Long = 0L,
    val otherCosts: Long = 0L,
    val totalCosts: Long = 0L,
    val profit: Long = 0L
)
