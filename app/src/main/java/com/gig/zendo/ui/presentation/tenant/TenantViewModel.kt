package com.gig.zendo.ui.presentation.tenant

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
class TenantViewModel @Inject constructor(
    private val tenantRepository: TenantRepository
): ViewModel() {

    private val _createTenantState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createTenantState: StateFlow<UiState<Any>> = _createTenantState

    private val _upImageState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val upImageState: StateFlow<UiState<String>> = _upImageState

    fun addTenant(tenant: Tenant) {
        _createTenantState.value = UiState.Loading
        viewModelScope.launch {
            _createTenantState.value = tenantRepository.addTenantWithImages(tenant)
        }
    }

    fun uploadImage(context: Context, uri: Uri) {
        _upImageState.value = UiState.Loading
        viewModelScope.launch {
            _upImageState.value = CloudinaryUploader.uploadImageFromUri(context, uri)
        }
    }
}