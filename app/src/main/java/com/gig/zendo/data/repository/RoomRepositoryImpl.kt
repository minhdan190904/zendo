package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.RoomRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RoomRepository {

    override suspend fun addRoom(room: Room): UiState<Unit> {
        return try {
            if (room.houseId.isEmpty()) return UiState.Failure("House ID is required")
            val roomId = room.id.ifEmpty { firestore.collection(Room.COLLECTION_NAME).document().id }
            firestore.collection(Room.COLLECTION_NAME).document(roomId).set(room.copy(id = roomId)).await()
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

    //one room can have a tenant behalf for many people of the room at the a time
    //if room exists, tenant can be null
    override suspend fun getRoomsWithTenant(houseId: String): UiState<List<Pair<Room, Tenant?>>> {
        return try {
            val snapshot = firestore.collection(Room.COLLECTION_NAME)
                .whereEqualTo(Room.FIELD_HOUSE_ID, houseId)
                .get()
                .await()

            val roomsWithTenants = snapshot.documents.mapNotNull { document ->
                try {
                    val room = document.toObject<Room>() ?: return@mapNotNull null
                    val tenantSnapshot = firestore.collection(Tenant.COLLECTION_NAME)
                        .whereEqualTo(Tenant.FIELD_ROOM_ID, room.id)
                        .whereEqualTo(Tenant.FIELD_ACTIVE, true)
                        .get()
                        .await()

                    val tenant = tenantSnapshot.documents.firstOrNull()?.toObject<Tenant>()
                    Pair(room, tenant)
                } catch (e: Exception) {
                    println("Failed to map document ${document.id}: ${e.message}")
                    null
                }
            }

            if (roomsWithTenants.isEmpty()) UiState.Empty else UiState.Success(roomsWithTenants)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch rooms with tenants")
        }
    }

    override suspend fun getRoomsWithTenants(houseId: String): UiState<List<Pair<Room, List<Tenant>>>> {
        return try {
            val snapshot = firestore.collection(Room.COLLECTION_NAME)
                .whereEqualTo(Room.FIELD_HOUSE_ID, houseId)
                .get()
                .await()

            val roomsWithTenants = snapshot.documents.mapNotNull { document ->
                try {
                    val room = document.toObject<Room>() ?: return@mapNotNull null
                    val tenantSnapshot = firestore.collection(Tenant.COLLECTION_NAME)
                        .whereEqualTo(Tenant.FIELD_ROOM_ID, room.id)
                        .get()
                        .await()

                    val tenants = tenantSnapshot.documents.mapNotNull { it.toObject<Tenant>() }
                    Pair(room, tenants)
                } catch (e: Exception) {
                    println("Failed to map document ${document.id}: ${e.message}")
                    null
                }
            }

            if (roomsWithTenants.isEmpty()) UiState.Empty else UiState.Success(roomsWithTenants)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch rooms with tenants")
        }
    }

    override suspend fun checkOutTenant(tenantId: String, endDate: String): UiState<Unit> {
        return try {
            val tenantRef = firestore.collection(Tenant.COLLECTION_NAME).document(tenantId)
            val tenant = tenantRef.get().await().toObject<Tenant>() ?: return UiState.Failure("Tenant not found")

            if (!tenant.active) return UiState.Failure("Tenant is already checked out")

            val updatedTenant = tenant.copy(active = false, endDate = endDate)
            tenantRef.set(updatedTenant).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to check out tenant")
        }
    }

}