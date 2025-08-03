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
){
    companion object {
        const val COLLECTION_NAME = "expense_records"
        const val FIELD_HOUSE_ID = "houseId"
        const val FIELD_DATE = "date"
    }
}