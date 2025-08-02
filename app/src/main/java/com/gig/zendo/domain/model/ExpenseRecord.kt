package com.gig.zendo.domain.model

data class ExpenseRecord(
    val id: String = "",
    val houseId: String = "",
    val description: String = "",
    val totalAmount: Long = 0L,
    val date: String = "",
    val waterExpense: Long = 0L,
    val electricExpense: Long = 0L,
    val otherExpenses: List<Expense> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)