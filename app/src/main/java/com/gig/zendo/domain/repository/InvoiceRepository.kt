package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.utils.UiState

interface InvoiceRepository {
    // Define methods for invoice operations, e.g., create, fetch, update, delete invoices
     suspend fun addInvoice(invoice: Invoice): UiState<Invoice>
     suspend fun getInvoicesInRoom(roomId: String): UiState<List<Invoice>>
     suspend fun getInvoicesInHouse(houseId: String): UiState<List<Invoice>>
     suspend fun updateStatusPaidForInvoices(listIdInvoice: List<String>): UiState<Unit>
     suspend fun updateStatusInvoice(invoiceId: String, status: InvoiceStatus): UiState<Unit>
     suspend fun deleteInvoice(invoiceId: String): UiState<Unit>
}