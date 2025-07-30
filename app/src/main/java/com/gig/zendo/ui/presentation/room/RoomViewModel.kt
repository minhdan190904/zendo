package com.gig.zendo.ui.presentation.room

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.RoomRepository
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val _createRoomState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createRoomState: StateFlow<UiState<Any>> = _createRoomState

    private val _roomsState = MutableStateFlow<UiState<List<Pair<Room, List<Tenant>>>>>(UiState.Empty)
    val roomsState: StateFlow<UiState<List<Pair<Room, List<Tenant>>>>> = _roomsState

    private val _deleteRoomState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteRoomState: StateFlow<UiState<Any>> = _deleteRoomState

    private val _checkOutTenantState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val checkOutTenantState: StateFlow<UiState<Any>> = _checkOutTenantState


    var roomName = mutableStateOf("")
        private set

    fun addRoom(name: String, houseId: String) {
        _createRoomState.value = UiState.Loading
        val room = Room(
            id = "",
            name = name,
            houseId = houseId
        )
        viewModelScope.launch {
            _createRoomState.value = roomRepository.addRoom(room)
        }
    }

    fun fetchRoomsWithTenants(houseId: String) {
        _roomsState.value = UiState.Loading
        viewModelScope.launch {
            _roomsState.value = roomRepository.getRoomsWithTenants(houseId)
        }
    }

    fun checkOutTenant(tenantId: String, endDate: String) {
        _checkOutTenantState.value = UiState.Loading
        viewModelScope.launch {
            _checkOutTenantState.value = roomRepository.checkOutTenant(tenantId, endDate)
        }
    }

    fun updateRoomName(name: String) {
        roomName.value = name
    }

    var currentRoomAndTenant = mutableStateOf<Pair<Room, Tenant>?>(null)
        private set


    fun updateRoomAndTenantCurrent(p: Pair<Room, Tenant>) {
        currentRoomAndTenant.value = p
    }
}