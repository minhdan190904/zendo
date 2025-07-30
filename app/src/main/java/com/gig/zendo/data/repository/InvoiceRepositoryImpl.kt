package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.repository.InvoiceRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): InvoiceRepository {
    override suspend fun addInvoice(invoice: Invoice): UiState<Unit> {
        return try {
            val invoiceId = invoice.id.ifEmpty { firebaseFirestore.collection(Invoice.COLLECTION_NAME).document().id }
            firebaseFirestore.collection(Invoice.COLLECTION_NAME).document(invoiceId).set(invoice.copy(id = invoiceId)).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add invoice")
        }
    }

    override suspend fun getInvoices(roomId: String): UiState<List<Invoice>> {
        return try {
            val invoices = firebaseFirestore.collection(Invoice.COLLECTION_NAME)
                .whereEqualTo(Invoice.FIELD_ROOM_ID, roomId)
                .get()
                .await()
                .toObjects(Invoice::class.java)
            if (invoices.isEmpty()) UiState.Empty
            else UiState.Success(invoices)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch invoices")
        }
    }

}