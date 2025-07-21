package com.gig.zendo.ui.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.repository.HouseRepository
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
) : ViewModel() {

    private val _createHouseState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createHouseState: StateFlow<UiState<Any>> = _createHouseState

    private val _houseState = MutableStateFlow<UiState<List<House>>>(UiState.Loading)
    val houseState: StateFlow<UiState<List<House>>> = _houseState

    private val _deleteHouseState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteHouseState: StateFlow<UiState<Any>> = _deleteHouseState

    private val _showDeleteDialog = MutableStateFlow<String?>(null)
    val showDeleteDialog: StateFlow<String?> = _showDeleteDialog

    var houseName = mutableStateOf("")
        private set

    var houseAddress = mutableStateOf("")
        private set

    fun addHouse(name: String, address: String, uid: String) {
        _createHouseState.value = UiState.Loading
        viewModelScope.launch {
            _createHouseState.value = houseRepository.addHouse(
                House(
                    id = "",
                    name = name,
                    address = address,
                    uid = uid
                )
            )
        }
    }

    fun showDeleteHouseDialog(houseId: String) {
        _showDeleteDialog.value = houseId
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = null
    }

    fun fetchHouses(uid: String) {
        _houseState.value = UiState.Loading
        viewModelScope.launch {
            _houseState.value = houseRepository.getHouses(uid)
        }
    }

    fun deleteHouse(houseId: String) {
        viewModelScope.launch {
            dismissDeleteDialog()
            _houseState.value = UiState.Loading
            _deleteHouseState.value = UiState.Loading
            _deleteHouseState.value = houseRepository.deleteHouse(houseId)
        }
    }

    fun clearDeleteState() {
        _deleteHouseState.value = UiState.Empty
    }

    fun updateHouseName(name: String) {
        houseName.value = name
    }

    fun updateHouseAddress(address: String) {
        houseAddress.value = address
    }
}