package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.utils.UiState
import java.io.File

interface TenantRepository {
    suspend fun addTenantWithImages(tenant: Tenant): UiState<String>
    suspend fun getTenants(roomId: String): UiState<List<Tenant>>
    suspend fun deleteTenant(id: String): UiState<Unit>
}