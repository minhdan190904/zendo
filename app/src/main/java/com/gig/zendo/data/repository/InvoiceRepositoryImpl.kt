package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.domain.repository.InvoiceRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): InvoiceRepository {
    //return invoice current created
    //create invoice and create id if id is empty
    override suspend fun addInvoice(invoice: Invoice): UiState<Invoice> {
        return try {
            if (invoice.roomId.isEmpty() || invoice.houseId.isEmpty()) {
                return UiState.Failure("Room ID and House ID are required")
            }
            val invoiceId = invoice.id.ifEmpty { firebaseFirestore.collection(Invoice.COLLECTION_NAME).document().id }
            val newInvoice = invoice.copy(id = invoiceId)
            firebaseFirestore.collection(Invoice.COLLECTION_NAME).document(invoiceId).set(newInvoice).await()
            UiState.Success(newInvoice)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to add invoice")
        }
    }

    override suspend fun getInvoicesInRoom(roomId: String): UiState<List<Invoice>> {
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

    override suspend fun getInvoicesInHouse(houseId: String): UiState<List<Invoice>> {
        return try {
            val invoices = firebaseFirestore.collection(Invoice.COLLECTION_NAME)
                .whereEqualTo(Invoice.FIELD_HOUSE_ID, houseId)
                .get()
                .await()
                .toObjects(Invoice::class.java)
            if (invoices.isEmpty()) UiState.Empty
            else UiState.Success(invoices)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to fetch invoices")
        }
    }

    //return all invoices
    override suspend fun updateStatusPaidForInvoices(listIdInvoice: List<String>): UiState<Unit> {
        return try {
            val batch = firebaseFirestore.batch()
            listIdInvoice.forEach { invoiceId ->
                val invoiceRef = firebaseFirestore.collection(Invoice.COLLECTION_NAME).document(invoiceId)
                batch.update(invoiceRef, Invoice.FIELD_INVOICE_STATUS, InvoiceStatus.PAID.name)
            }
            batch.commit().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update status for invoices")
        }
    }

    override suspend fun updateStatusInvoice(invoiceId: String, status: InvoiceStatus): UiState<Unit> {
        return try {
            val invoiceRef = firebaseFirestore.collection(Invoice.COLLECTION_NAME).document(invoiceId)
            invoiceRef.update(Invoice.FIELD_INVOICE_STATUS, status.name).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "Failed to update invoice status")
        }
    }

}