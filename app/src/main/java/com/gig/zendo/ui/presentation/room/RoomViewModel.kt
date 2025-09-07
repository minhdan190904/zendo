package com.gig.zendo.ui.presentation.room

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.domain.repository.RoomRepository
import com.gig.zendo.domain.validation.FieldState
import com.gig.zendo.domain.validation.RoomFormUiState
import com.gig.zendo.domain.validation.TextFieldValidator
import com.gig.zendo.domain.validation.ValidationResult
import com.gig.zendo.domain.validation.errorOrNull
import com.gig.zendo.utils.CloudinaryUploader
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

    private val _roomFormState = MutableStateFlow(RoomFormUiState())
    val roomFormState: StateFlow<RoomFormUiState> = _roomFormState

    var selectedRoom by mutableStateOf<Room?>(null)
    var selectedTenant by mutableStateOf<Tenant?>(null)

    fun initForCreate() {
        selectedRoom = null
        _roomFormState.value = RoomFormUiState()
    }

    fun initForEdit(room: Room?) {
        selectedRoom = room
        val v = TextFieldValidator.validate(
            room?.name.orEmpty()
        )

        _roomFormState.value = RoomFormUiState(
            name = FieldState(text = room?.name.orEmpty(), error = v.errorOrNull()),
            canSubmit = v is ValidationResult.Valid
        )
    }

    fun onRoomNameChange(newValue: String) {
        val v = TextFieldValidator.validate(newValue)
        _roomFormState.value = _roomFormState.value.copy(
            name = FieldState(text = newValue, error = v.errorOrNull()),
            canSubmit = v is ValidationResult.Valid
        )
    }

    fun updateRoom(houseId: String) {
        val current = _roomFormState.value

        _createRoomState.value = UiState.Loading

        val room = Room(
            id = selectedRoom?.id ?: "",
            name = current.name.text.trim(),
            houseId = houseId,
            createdAt = selectedRoom?.createdAt ?: System.currentTimeMillis(),
        )

        viewModelScope.launch {
            _createRoomState.value = roomRepository.addAndUpdateRoom(room)
        }
    }

    private val _createRoomState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createRoomState: StateFlow<UiState<Any>> = _createRoomState

    private val _roomsState =
        MutableStateFlow<UiState<List<Pair<Room, List<Tenant>>>>>(UiState.Empty)
    val roomsState: StateFlow<UiState<List<Pair<Room, List<Tenant>>>>> = _roomsState

    private val _deleteRoomState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteRoomState: StateFlow<UiState<Any>> = _deleteRoomState

    private val _checkOutTenantState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val checkOutTenantState: StateFlow<UiState<Any>> = _checkOutTenantState

    private val _upImageState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val upImageState: StateFlow<UiState<String>> = _upImageState

    private val _serviceRecordsByRoomIdState =
        MutableStateFlow<UiState<List<ServiceRecord>>>(UiState.Empty)
    val serviceRecordsByRoomIdState: StateFlow<UiState<List<ServiceRecord>>> =
        _serviceRecordsByRoomIdState

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

    fun fetchServiceRecordsByRoomId(roomId: String) {
        _serviceRecordsByRoomIdState.value = UiState.Loading
        viewModelScope.launch {
            _serviceRecordsByRoomIdState.value = roomRepository.getServiceRecordsByRoomId(roomId)
        }
    }

    fun uploadImage(context: Context, uri: Uri) {
        _upImageState.value = UiState.Loading
        viewModelScope.launch {
            _upImageState.value = CloudinaryUploader.uploadImageFromUri(context, uri)
        }
    }

    fun clearCreateRoomState() {
        _createRoomState.value = UiState.Empty
    }

    fun clearCheckOutTenantState() {
        _checkOutTenantState.value = UiState.Empty
    }

    var currentRoomAndTenant = mutableStateOf<Pair<Room, Tenant>?>(null)
        private set

    fun updateRoomAndTenantCurrent(p: Pair<Room, Tenant>) {
        currentRoomAndTenant.value = p
    }
}