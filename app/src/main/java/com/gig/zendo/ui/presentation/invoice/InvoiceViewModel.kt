package com.gig.zendo.ui.presentation.invoice

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.domain.repository.InvoiceRepository
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

    private val _createInvoiceState = MutableStateFlow<UiState<Invoice>>(UiState.Empty)
    val createInvoiceState: StateFlow<UiState<Invoice>> = _createInvoiceState

    private val _invoicesStateInRoom = MutableStateFlow<UiState<List<Invoice>>>(UiState.Empty)
    val invoicesStateInRoom: StateFlow<UiState<List<Invoice>>> = _invoicesStateInRoom

    private val _invoicesStateInHouse = MutableStateFlow<UiState<List<Invoice>>>(UiState.Empty)
    val invoicesStateInHouse: StateFlow<UiState<List<Invoice>>> = _invoicesStateInHouse

    private val _updateStatusPaidState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val updateStatusPaidState: StateFlow<UiState<Unit>> = _updateStatusPaidState

    private val _updateStatusInvoiceState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val updateStatusInvoiceState: StateFlow<UiState<Unit>> = _updateStatusInvoiceState

    private val _deleteInvoiceState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val deleteInvoiceState: StateFlow<UiState<Unit>> = _deleteInvoiceState

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

    fun getInvoicesInRoom(roomId: String) {
        _invoicesStateInRoom.value = UiState.Loading
        viewModelScope.launch {
            _invoicesStateInRoom.value = invoiceRepository.getInvoicesInRoom(roomId)
        }
    }

    fun deleteInvoice(invoiceId: String) {
        _deleteInvoiceState.value = UiState.Loading
        viewModelScope.launch {
            _deleteInvoiceState.value = invoiceRepository.deleteInvoice(invoiceId)
        }
    }

    fun getInvoicesInHouse(houseId: String) {
        _invoicesStateInHouse.value = UiState.Loading
        viewModelScope.launch {
            _invoicesStateInHouse.value = invoiceRepository.getInvoicesInHouse(houseId)
        }
    }

    fun updateStatusPaidForInvoices(listIdInvoice: List<String>) {
        _updateStatusPaidState.value = UiState.Loading
        viewModelScope.launch {
            _updateStatusPaidState.value = invoiceRepository.updateStatusPaidForInvoices(listIdInvoice)
        }
    }

    fun updateStatusInvoice(invoiceId: String, status: InvoiceStatus) {
        _updateStatusInvoiceState.value = UiState.Loading
        viewModelScope.launch {
            _updateStatusInvoiceState.value = invoiceRepository.updateStatusInvoice(invoiceId, status)
        }
    }

    fun resetStateUpdateStatus() {
        _updateStatusInvoiceState.value = UiState.Empty
    }

    fun resetStateCreateInvoice() {
        _createInvoiceState.value = UiState.Empty
    }

    fun resetStateUpImage() {
        _upImageState.value = UiState.Empty
    }

    fun resetStateDeleteInvoice() {
        _deleteInvoiceState.value = UiState.Empty
    }
}