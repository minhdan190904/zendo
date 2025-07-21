package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.Room
import com.gig.zendo.utils.UiState

interface RoomRepository {
    suspend fun addRoom(room: Room): UiState<Unit>
    suspend fun getRooms(houseId: String): UiState<List<Room>>
    suspend fun deleteRoom(id: String): UiState<Unit>
}