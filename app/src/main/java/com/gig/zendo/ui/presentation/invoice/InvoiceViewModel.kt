package com.gig.zendo.ui.presentation.invoice

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Tenant
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
    private val tenantRepository: TenantRepository
): ViewModel() {

    private val _upImageState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val upImageState: StateFlow<UiState<String>> = _upImageState

    fun uploadImage(context: Context, uri: Uri) {
        _upImageState.value = UiState.Loading
        viewModelScope.launch {
            _upImageState.value = CloudinaryUploader.uploadImageFromUri(context, uri)
        }
    }
}