package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.utils.RoomMenuAction
import com.gig.zendo.utils.UiState

interface InvoiceRepository {
    // Define methods for invoice operations, e.g., create, fetch, update, delete invoices
     suspend fun addInvoice(invoice: Invoice): UiState<Unit>
     suspend fun getInvoices(roomId: String): UiState<List<Invoice>>
    // suspend fun updateInvoice(invoice: Invoice): Result<Unit>
    // suspend fun deleteInvoice(invoiceId: String): Result<Unit>
}