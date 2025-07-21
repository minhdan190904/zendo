package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.House
import com.gig.zendo.utils.UiState

interface HouseRepository {
    suspend fun addHouse(house: House): UiState<Unit>
    suspend fun getHouses(uid: String): UiState<List<House>>
    suspend fun deleteHouse(houseId: String): UiState<Unit>
    suspend fun updateHouse(house: House): UiState<Unit>
}
