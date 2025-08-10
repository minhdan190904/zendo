package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.domain.model.FinancialReport
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.utils.UiState

interface HouseRepository {
    suspend fun addAndUpdateHouse(house: House): UiState<Unit>
    suspend fun getHouses(uid: String): UiState<List<House>>
    suspend fun deleteHouse(houseId: String): UiState<Unit>
    suspend fun updateHouse(house: House): UiState<Unit>
    suspend fun getHouseById(houseId: String): UiState<House>
    suspend fun updateHouseServices(
        houseId: String,
        rentService: Service?,
        electricService: Service?,
        waterService: Service?,
        billingDay: Int?
    ): UiState<House>

    suspend fun getServiceRecordsByHouseId(houseId: String): UiState<List<ServiceRecord>>
    suspend fun createServiceRecord(serviceRecord: ServiceRecord): UiState<Unit>
    suspend fun createExpenseRecord(expenseRecord: ExpenseRecord): UiState<Unit>
    suspend fun getExpenseRecords(houseId: String): UiState<List<ExpenseRecord>>
    suspend fun deleteExpenseRecord(expenseId: String): UiState<Unit>
    suspend fun getFinancialReportForAllMonths(houseId: String, year: Int): UiState<List<FinancialReport>> // list only contains 12 financial reports, one for each month
}
