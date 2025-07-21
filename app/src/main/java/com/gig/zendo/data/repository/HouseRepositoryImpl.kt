package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.repository.HouseRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HouseRepository {

    override suspend fun addHouse(house: House): UiState<Unit> {
        return try {
            val houseId = house.id.ifEmpty { firestore.collection(House.COLLECTION_NAME).document().id }
            firestore.collection(House.COLLECTION_NAME).document(houseId).set(house.copy(id = houseId)).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add house")
        }
    }

    override suspend fun getHouses(uid: String): UiState<List<House>> {
        return try {
            val snapshot = firestore.collection(House.COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .get()
                .await()

            val houses = snapshot.documents.mapNotNull { document ->
                try {
                    document.toObject<House>()
                } catch (e: Exception) {
                    println("Failed to map document ${document.id}: ${e.message}")
                    null
                }
            }

            if (houses.isEmpty()) UiState.Empty else UiState.Success(houses)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch houses")
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
            val houseId = house.id.ifEmpty { throw IllegalArgumentException("House ID cannot be empty") }
            firestore.collection(House.COLLECTION_NAME).document(houseId).set(house).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update house")
        }
    }
}