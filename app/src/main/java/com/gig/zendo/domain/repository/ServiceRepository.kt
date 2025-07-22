package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Service
import com.gig.zendo.utils.UiState

interface ServiceRepository {
     suspend fun addService(service: Service): UiState<Any>
     suspend fun getServices(houseId: String): UiState<List<Service>>
     suspend fun deleteService(serviceId: String): UiState<Any>
     suspend fun updateService(service: Service): UiState<Any>
}