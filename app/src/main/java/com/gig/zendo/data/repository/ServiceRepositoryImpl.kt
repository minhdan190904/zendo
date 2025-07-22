package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.repository.ServiceRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ServiceRepository {
    override suspend fun addService(service: Service): UiState<Any> {
        return try {
            if (service.houseId.isEmpty()) return UiState.Failure("House ID is required")
            val serviceId = service.id.ifEmpty { firestore.collection(Service.COLLECTION_NAME).document().id }
            firestore.collection(Service.COLLECTION_NAME).document(serviceId).set(service.copy(id = serviceId)).await()
            UiState.Success(Any())
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add service")
        }
    }

    override suspend fun getServices(houseId: String): UiState<List<Service>> {
        return try {
            val snapshot = firestore.collection(Service.COLLECTION_NAME)
                .whereEqualTo(Service.FIELD_HOUSE_ID, houseId)
                .get()
                .await()
            val services = snapshot.documents.mapNotNull { document ->
                try {
                    document.toObject<Service>()
                } catch (e: Exception) {
                    println("Failed to map document ${document.id}: ${e.message}")
                    null
                }
            }
            if (services.isEmpty()) UiState.Empty else UiState.Success(services)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch services")
        }
    }

    override suspend fun deleteService(serviceId: String): UiState<Any> {
        return try {
            firestore.collection(Service.COLLECTION_NAME).document(serviceId).delete().await()
            UiState.Success(Any())
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to delete service")
        }
    }

    override suspend fun updateService(service: Service): UiState<Any> {
        return try {
            if (service.id.isEmpty()) return UiState.Failure("Service ID is required")
            firestore.collection(Service.COLLECTION_NAME).document(service.id).set(service).await()
            UiState.Success(Any())
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update service")
        }
    }
}