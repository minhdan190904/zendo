package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Room
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
}