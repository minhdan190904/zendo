package com.gig.zendo.ui.presentation.service

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.data.repository.ServiceRepositoryImpl
import com.gig.zendo.domain.model.Service
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepositoryImpl
) : ViewModel() {
    private val _createServiceState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createServiceState: StateFlow<UiState<Any>> = _createServiceState

    private val _servicesState = MutableStateFlow<UiState<List<Service>>>(UiState.Loading)
    val servicesState: StateFlow<UiState<List<Service>>> = _servicesState

    private val _deleteServiceState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteServiceState: StateFlow<UiState<Any>> = _deleteServiceState

    fun addService(service: Service) {
        _createServiceState.value = UiState.Loading
        viewModelScope.launch {
            _createServiceState.value = serviceRepository.addService(service)
        }
    }

    fun fetchServices(houseId: String) {
        _servicesState.value = UiState.Loading
        viewModelScope.launch {
            _servicesState.value = serviceRepository.getServices(houseId)
        }
    }
}