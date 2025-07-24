package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.utils.UiState

interface RoomRepository {
    suspend fun addRoom(room: Room): UiState<Unit>
    suspend fun getRooms(houseId: String): UiState<List<Room>>
    suspend fun deleteRoom(id: String): UiState<Unit>

    //Only one room can have a tenant behalf for many people of the room at the a time
    suspend fun getRoomsWithTenant(houseId: String): UiState<List<Pair<Room, Tenant?>>>

    //Include all tenants in the rooms tented, include active and inactive tenants
    suspend fun getRoomsWithTenants(houseId: String): UiState<List<Pair<Room, List<Tenant>>>>

    //deactivate tenant
    suspend fun checkOutTenant(
        tenantId: String,
        endDate: String
    ): UiState<Unit>
}