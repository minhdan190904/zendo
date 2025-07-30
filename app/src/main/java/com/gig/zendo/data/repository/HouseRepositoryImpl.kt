package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
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

    override suspend fun getHouseById(houseId: String): UiState<House> {
        return try {
            val document = firestore.collection(House.COLLECTION_NAME).document(houseId).get().await()
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

            val house = houseSnapshot.toObject<House>() ?: return UiState.Failure("House data is null")

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

}