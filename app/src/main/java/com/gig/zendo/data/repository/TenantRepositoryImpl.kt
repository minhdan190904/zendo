
package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.TenantRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class TenantRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TenantRepository {

    override suspend fun addTenantWithImages(tenant: Tenant): UiState<String> {
        return try {
            val tenantId = tenant.id.ifEmpty { firestore.collection(Tenant.COLLECTION_NAME).document().id }

            val tenantData = tenant.copy(id = tenantId)
            firestore.collection(Tenant.COLLECTION_NAME)
                .document(tenantId)
                .set(tenantData)
                .await()

            UiState.Success(tenantId)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add tenant")
        }
    }

    override suspend fun getTenants(roomId: String): UiState<List<Tenant>> {
        return try {
            val snapshot = firestore.collection(Tenant.COLLECTION_NAME)
                .whereEqualTo(Tenant.FIELD_ROOM_ID, roomId)
                .get()
                .await()
            val tenants = snapshot.documents.mapNotNull { document ->
                try {
                    document.toObject(Tenant::class.java)?.copy(id = document.id)
                } catch (e: Exception) {
                    null
                }
            }
            if (tenants.isEmpty()) UiState.Empty else UiState.Success(tenants)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch tenants")
        }
    }

    override suspend fun deleteTenant(id: String): UiState<Unit> {
        return try {
            firestore.collection(Tenant.COLLECTION_NAME)
                .document(id)
                .delete()
                .await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to delete tenant")
        }
    }
}
