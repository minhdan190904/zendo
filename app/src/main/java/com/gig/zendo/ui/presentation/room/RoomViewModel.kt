package com.gig.zendo.ui.presentation.room

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Room
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

    private val _roomState = MutableStateFlow<UiState<List<Room>>>(UiState.Loading)
    val roomState: StateFlow<UiState<List<Room>>> = _roomState

    private val _deleteRoomState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteRoomState: StateFlow<UiState<Any>> = _deleteRoomState

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

    fun fetchRooms(houseId: String) {
        _roomState.value = UiState.Loading
        viewModelScope.launch {
            _roomState.value = roomRepository.getRooms(houseId)
        }
    }

    fun updateRoomName(name: String) {
        roomName.value = name
    }
}