package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.RoomRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RoomRepository {

    override suspend fun addAndUpdateRoom(room: Room): UiState<Unit> {
        return try {
            if (room.houseId.isEmpty()) return UiState.Failure("House ID is required")
            val roomId =
                room.id.ifEmpty { firestore.collection(Room.COLLECTION_NAME).document().id }
            firestore.collection(Room.COLLECTION_NAME).document(roomId).set(room.copy(id = roomId))
                .await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add room")
        }
    }

    override suspend fun getRooms(houseId: String): UiState<List<Room>> {
        return try {
            val snapshot = firestore.collection(Room.COLLECTION_NAME)
                .whereEqualTo(Room.FIELD_HOUSE_ID, houseId)
                .get()
                .await()
            val rooms = snapshot.documents.mapNotNull { document ->
                try {
                    document.toObject<Room>()
                } catch (e: Exception) {
                    println("Failed to map document ${document.id}: ${e.message}")
                    null
                }
            }
            if (rooms.isEmpty()) UiState.Empty else UiState.Success(rooms)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch rooms")
        }
    }

    override suspend fun deleteRoom(id: String): UiState<Unit> {
        return try {
            firestore.collection(Room.COLLECTION_NAME).document(id).delete().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to delete room")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getRoomsWithTenants(houseId: String): UiState<List<Pair<Room, List<Tenant>>>> {
        return try {
            val roomsSnapshot = firestore.collection(Room.COLLECTION_NAME)
                .whereEqualTo(Room.FIELD_HOUSE_ID, houseId)
                .get()
                .await()

            val rooms = roomsSnapshot.documents.mapNotNull { it.toObject<Room>() }
            if (rooms.isEmpty()) return UiState.Empty

            val roomIds = rooms.map { it.id }.filter { it.isNotEmpty() }

            val chunkSize = 10

            val invoiceDeferred = roomIds.chunked(chunkSize).map { chunk ->
                GlobalScope.async {
                    firestore.collection(Invoice.COLLECTION_NAME)
                        .whereIn(Invoice.FIELD_ROOM_ID, chunk)
                        .whereEqualTo(Invoice.FIELD_INVOICE_STATUS, InvoiceStatus.NOT_PAID)
                        .get()
                        .await()
                        .documents.mapNotNull { it.toObject<Invoice>() }
                }
            }

            val tenantDeferred = roomIds.chunked(chunkSize).map { chunk ->
                GlobalScope.async {
                    firestore.collection(Tenant.COLLECTION_NAME)
                        .whereIn(Tenant.FIELD_ROOM_ID, chunk)
                        .get()
                        .await()
                        .documents.mapNotNull { it.toObject<Tenant>() }
                }
            }

            val allInvoices = invoiceDeferred
                .map { it.await() }
                .flatten()

            val allTenants = tenantDeferred
                .map { it.await() }
                .flatten()

            val invoicesByRoom = allInvoices.groupBy { it.roomId }
            val tenantsByRoom = allTenants.groupBy { it.roomId }

            val result = rooms.map { room ->
                val notPaid = invoicesByRoom[room.id].orEmpty()
                val numberOfNotPaidInvoice = notPaid.size
                val outstandingAmount = notPaid.sumOf { it.totalAmount }

                val updatedRoom = room.copy(
                    numberOfNotPaidInvoice = numberOfNotPaidInvoice,
                    outstandingAmount = outstandingAmount
                )

                val tenants = tenantsByRoom[room.id].orEmpty()
                Pair(updatedRoom, tenants)
            }

            UiState.Success(result)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch rooms with tenants")
        }
    }


    //add update room empty = true
    override suspend fun checkOutTenant(tenantId: String, endDate: String): UiState<Unit> {
        return try {
            // Update tenant to inactive
            val tenantRef = firestore.collection(Tenant.COLLECTION_NAME).document(tenantId)
            val tenant = tenantRef.get().await().toObject<Tenant>()
                ?: return UiState.Failure("Tenant not found")

            if (tenant.active) {
                tenantRef.update(
                    mapOf(
                        Tenant.FIELD_ACTIVE to false,
                        Tenant.FIELD_END_DATE to endDate
                    )
                ).await()

            }

            // Update room to empty
            val roomRef = firestore.collection(Room.COLLECTION_NAME).document(tenant.roomId)
            roomRef.update(Room.FIELD_EMPTY, true).await()

            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to check out tenant")
        }
    }

    override suspend fun getServiceRecordsByRoomId(roomId: String): UiState<List<ServiceRecord>> {
        return try {
            val serviceRecordSnapshot = firestore.collection(ServiceRecord.COLLECTION_NAME)
                .whereEqualTo(ServiceRecord.FIELD_ROOM_ID, roomId).get().await()

            val serviceRecords =
                serviceRecordSnapshot.documents.mapNotNull { it.toObject<ServiceRecord>() }
            serviceRecords.sortedByDescending { it.createdAt }
            if (serviceRecords.isEmpty()) UiState.Empty else UiState.Success(serviceRecords)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch service records")
        }
    }

}