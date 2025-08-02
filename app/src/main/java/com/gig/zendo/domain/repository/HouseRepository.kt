package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.utils.UiState

interface HouseRepository {
    suspend fun addHouse(house: House): UiState<Unit>
    suspend fun getHouses(uid: String): UiState<List<House>>
    suspend fun deleteHouse(houseId: String): UiState<Unit>
    suspend fun updateHouse(house: House): UiState<Unit>
    suspend fun getHouseById(houseId: String): UiState<House>
    suspend fun updateHouseServices(
        houseId: String,
        rentService: Service?,
        electricService: Service?,
        waterService: Service?,
        billingDay: Int?
    ): UiState<House>
    suspend fun getServiceRecords(houseId: String): UiState<List<ServiceRecord>>
    suspend fun createServiceRecord(serviceRecord: ServiceRecord): UiState<Unit>
}
