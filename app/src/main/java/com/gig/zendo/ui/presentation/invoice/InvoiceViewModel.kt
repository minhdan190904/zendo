package com.gig.zendo.ui.presentation.invoice

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.InvoiceRepository
import com.gig.zendo.domain.repository.TenantRepository
import com.gig.zendo.utils.CloudinaryUploader
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository
): ViewModel() {

    private val _upImageState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val upImageState: StateFlow<UiState<String>> = _upImageState

    private val _createInvoiceState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val createInvoiceState: StateFlow<UiState<Unit>> = _createInvoiceState

    private val invoicesState = MutableStateFlow<UiState<List<Invoice>>>(UiState.Empty)
    val invoices: StateFlow<UiState<List<Invoice>>> = invoicesState

    fun uploadImage(context: Context, uri: Uri) {
        _upImageState.value = UiState.Loading
        viewModelScope.launch {
            _upImageState.value = CloudinaryUploader.uploadImageFromUri(context, uri)
        }
    }

    fun addInvoice(invoice: Invoice){
        _createInvoiceState.value = UiState.Loading
        viewModelScope.launch {
            _createInvoiceState.value = invoiceRepository.addInvoice(invoice)
        }
    }

    fun getInvoices(roomId: String) {
        invoicesState.value = UiState.Loading
        viewModelScope.launch {
            invoicesState.value = invoiceRepository.getInvoices(roomId)
        }
    }
}