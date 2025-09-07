package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.domain.model.FinancialReport
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.domain.repository.HouseRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HouseRepository {

    override suspend fun addAndUpdateHouse(house: House): UiState<Unit> {
        return try {
            val houseId =
                house.id.ifEmpty { firestore.collection(House.COLLECTION_NAME).document().id }
            firestore.collection(House.COLLECTION_NAME).document(houseId)
                .set(house.copy(id = houseId)).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add house")
        }
    }

    override suspend fun getHouses(uid: String): UiState<List<House>> {
        return try {
            val houseSnapshot = firestore.collection(House.COLLECTION_NAME).whereEqualTo(House.FIELD_UID, uid).get().await()
            val roomSnapshot = firestore.collection(Room.COLLECTION_NAME).get().await()
            val invoiceSnapshot = firestore.collection(Invoice.COLLECTION_NAME).get().await()
            val expenseRecordSnapshot = firestore.collection(ExpenseRecord.COLLECTION_NAME).get().await()

            val houses = houseSnapshot.documents.mapNotNull { it.toObject<House>() }
            val rooms = roomSnapshot.documents.mapNotNull { it.toObject<Room>() }
            val invoices = invoiceSnapshot.documents.mapNotNull { it.toObject<Invoice>() }
            val expenseRecord = expenseRecordSnapshot.documents.mapNotNull { it.toObject<ExpenseRecord>() }

            val houseList = houses.map { house ->
                val houseRooms = rooms.filter { it.houseId == house.id }
                val houseInvoices = invoices.filter { it.houseId == house.id }
                val houseExpenseRecord = expenseRecord.filter { it.houseId == house.id }

                val numberOfRoom = houseRooms.size
                val numberOfEmptyRoom = houseRooms.count { it.empty }
                val notPaidInvoices = houseInvoices.filter { it.status == InvoiceStatus.NOT_PAID }

                val roomIdsWithNotPaidInvoices = notPaidInvoices.map { it.roomId }.distinct()
                val numberOfNotPaidRoom =
                    houseRooms.count { roomIdsWithNotPaidInvoices.contains(it.id) }

                val unpaidAmount = notPaidInvoices.sumOf { it.totalAmount }

                val numberOfNotPaidInvoice = notPaidInvoices.size
                val numberOfInvoice = houseInvoices.size

                val currentMonthYear =
                    SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date())

                val monthlyRevenue =
                    houseInvoices.filter { it.date.endsWith(currentMonthYear) && it.status == InvoiceStatus.PAID }
                        .sumOf { it.totalAmount }

                val monthlyExpense = houseExpenseRecord.filter { it.date.endsWith(currentMonthYear) }
                    .sumOf { it.totalAmount }

                house.copy(
                    numberOfRoom = numberOfRoom,
                    numberOfEmptyRoom = numberOfEmptyRoom,
                    numberOfNotPaidRoom = numberOfNotPaidRoom,
                    unpaidAmount = unpaidAmount,
                    monthlyRevenue = monthlyRevenue,
                    numberOfInvoice = numberOfInvoice,
                    numberOfNotPaidInvoice = numberOfNotPaidInvoice,
                    monthlyExpense = monthlyExpense
                )
            }

            houseList.sortedByDescending { it.createdAt }

            if (houseList.isEmpty()) UiState.Empty else UiState.Success(houseList)

        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch houses with stats")
        }
    }


    override suspend fun deleteHouse(houseId: String): UiState<Unit> {
        return try {
            firestore.collection(House.COLLECTION_NAME).document(houseId).delete().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to delete house")
        }
    }

    override suspend fun updateHouse(house: House): UiState<Unit> {
        return try {
            val houseId =
                house.id.ifEmpty { throw IllegalArgumentException("House ID cannot be empty") }
            firestore.collection(House.COLLECTION_NAME).document(houseId).set(house).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update house")
        }
    }

    override suspend fun getHouseById(houseId: String): UiState<House> {
        return try {
            val document =
                firestore.collection(House.COLLECTION_NAME).document(houseId).get().await()
            if (document.exists()) {
                val house = document.toObject<House>()
                if (house != null) {
                    UiState.Success(house)
                } else {
                    UiState.Failure("House not found")
                }
            } else {
                UiState.Failure("House not found")
            }
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch house by ID")
        }
    }

    override suspend fun updateHouseServices(
        houseId: String,
        rentService: Service?,
        electricService: Service?,
        waterService: Service?,
        billingDay: Int?
    ): UiState<House> {
        return try {
            val houseRef = firestore.collection(House.COLLECTION_NAME).document(houseId)
            val houseSnapshot = houseRef.get().await()

            if (!houseSnapshot.exists()) {
                return UiState.Failure("House not found")
            }

            val house =
                houseSnapshot.toObject<House>() ?: return UiState.Failure("House data is null")

            val updatedHouse = house.copy(
                rentService = rentService ?: house.rentService,
                electricService = electricService ?: house.electricService,
                waterService = waterService ?: house.waterService,
                billingDay = billingDay ?: house.billingDay
            )

            houseRef.set(updatedHouse).await()
            UiState.Success(updatedHouse)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update house services")
        }
    }

    override suspend fun getServiceRecordsByHouseId(houseId: String): UiState<List<ServiceRecord>> {
        return try {
            val serviceRecordSnapshot = firestore.collection(ServiceRecord.COLLECTION_NAME)
                .whereEqualTo(ServiceRecord.FIELD_HOUSE_ID, houseId).get().await()

            val serviceRecords =
                serviceRecordSnapshot.documents.mapNotNull { it.toObject<ServiceRecord>() }
            serviceRecords.sortedByDescending { it.createdAt }
            if (serviceRecords.isEmpty()) UiState.Empty else UiState.Success(serviceRecords)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch service records")
        }
    }

    override suspend fun createServiceRecord(serviceRecord: ServiceRecord): UiState<Unit> {
        return try {
            val serviceRecordId = serviceRecord.id.ifEmpty {
                firestore.collection(ServiceRecord.COLLECTION_NAME).document().id
            }
            firestore.collection(ServiceRecord.COLLECTION_NAME).document(serviceRecordId)
                .set(serviceRecord.copy(id = serviceRecordId)).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to create service record")
        }
    }



    override suspend fun createExpenseRecord(expenseRecord: ExpenseRecord): UiState<Unit> {
        return try {
            val expenseRecordId = expenseRecord.id.ifEmpty {
                firestore.collection(ExpenseRecord.COLLECTION_NAME).document().id
            }
            firestore.collection(ExpenseRecord.COLLECTION_NAME).document(expenseRecordId)
                .set(expenseRecord.copy(id = expenseRecordId)).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to create expense record")
        }
    }

    override suspend fun getExpenseRecords(houseId: String): UiState<List<ExpenseRecord>> {
        return try {
            val expenseRecordSnapshot = firestore.collection(ExpenseRecord.COLLECTION_NAME)
                .whereEqualTo(ExpenseRecord.FIELD_HOUSE_ID, houseId).get().await()

            val expenseRecords =
                expenseRecordSnapshot.documents.mapNotNull { it.toObject<ExpenseRecord>() }

            if (expenseRecords.isEmpty()) UiState.Empty else UiState.Success(expenseRecords)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch expense records")
        }
    }

    override suspend fun deleteExpenseRecord(expenseId: String): UiState<Unit> {
        return try {
            firestore.collection(ExpenseRecord.COLLECTION_NAME).document(expenseId).delete().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to delete expense record")
        }
    }

    override suspend fun getFinancialReportForAllMonths(
        houseId: String, year: Int
    ): UiState<List<FinancialReport>> {
        return try {
            val invoiceSnapshot = firestore.collection(Invoice.COLLECTION_NAME)
                .whereEqualTo(Invoice.FIELD_HOUSE_ID, houseId).get().await()
            val invoices = invoiceSnapshot.documents.mapNotNull { it.toObject<Invoice>() }

            val roomSnapshot = firestore.collection(Room.COLLECTION_NAME)
                .whereEqualTo(Room.FIELD_HOUSE_ID, houseId).get().await()
            val rooms = roomSnapshot.documents.mapNotNull { it.toObject<Room>() }

            val expenseRecordSnapshot = firestore.collection(ExpenseRecord.COLLECTION_NAME)
                .whereEqualTo(ExpenseRecord.FIELD_HOUSE_ID, houseId).get().await()
            val expenseRecords =
                expenseRecordSnapshot.documents.mapNotNull { it.toObject<ExpenseRecord>() }

            val financialReports = mutableListOf<FinancialReport>()

            val totalRooms = rooms.size

            for (month in 1..12) {
                val monthString = if (month < 10) "0$month" else month.toString()
                val monthYearString = "$monthString/$year"

                var monthInvoices = invoices.filter { invoice ->
                    invoice.date.endsWith(monthYearString)
                }

                val monthExpenseRecords = expenseRecords.filter { expense ->
                    expense.date.endsWith(monthYearString)
                }

                val rentedRooms = monthInvoices.map { it.roomId }.distinct().size
                val vacantRooms = totalRooms - rentedRooms

                monthInvoices = monthInvoices.filter { it.status == InvoiceStatus.PAID }

                val roomRevenue = monthInvoices.sumOf {
                        if (it.rentService.chargeMethod == ChargeMethod.FIXED) it.rentService.chargeValue
                        else it.rentService.chargeValue * it.tenant.numberOfOccupants
                    }
                val electricityRevenue = monthInvoices.sumOf {
                    when (it.electricService.chargeMethod) {
                        ChargeMethod.BY_CONSUMPTION ->
                            it.electricService.chargeValue * (it.newNumberElectric - it.oldNumberElectric)
                        else ->
                            it.electricService.chargeValue * it.tenant.numberOfOccupants
                    }
                }

                val waterRevenue = monthInvoices.sumOf {
                    when (it.waterService.chargeMethod) {
                        ChargeMethod.BY_CONSUMPTION ->
                            it.waterService.chargeValue * (it.newNumberWater - it.oldNumberWater)
                        else ->
                            it.waterService.chargeValue * it.tenant.numberOfOccupants
                    }
                }

                val otherRevenue = monthInvoices.sumOf { invoice ->
                    invoice.otherServices.sumOf { service ->
                        when (service.chargeMethod) {
                            ChargeMethod.FIXED -> service.chargeValue
                            else -> service.chargeValue * invoice.tenant.numberOfOccupants
                        }
                    }
                }


                val totalRevenue = roomRevenue + electricityRevenue + waterRevenue + otherRevenue

                val electricityCost = monthExpenseRecords.sumOf { it.electricExpense }
                val waterCost = monthExpenseRecords.sumOf { it.waterExpense }
                val otherCosts = monthExpenseRecords.sumOf { it.otherExpenses.sumOf { it.amount } }

                val totalCosts = electricityCost + waterCost + otherCosts
                val profit = totalRevenue - totalCosts

                financialReports.add(
                    FinancialReport(
                        month = month,
                        year = year,
                        totalRooms = totalRooms,
                        vacantRooms = vacantRooms,
                        rentedRooms = rentedRooms,
                        roomRevenue = roomRevenue,
                        electricityRevenue = electricityRevenue,
                        waterRevenue = waterRevenue,
                        otherRevenue = otherRevenue,
                        totalRevenue = totalRevenue,
                        electricityCost = electricityCost,
                        waterCost = waterCost,
                        otherCosts = otherCosts,
                        totalCosts = totalCosts,
                        profit = profit
                    )
                )
            }

            if (financialReports.isEmpty()) UiState.Empty else UiState.Success(financialReports)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch financial report for all months")
        }
    }


}