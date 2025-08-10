package com.gig.zendo.ui.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.domain.model.FinancialReport
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.model.ServiceRecord
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

    private val _housesState = MutableStateFlow<UiState<List<House>>>(UiState.Loading)
    val housesState: StateFlow<UiState<List<House>>> = _housesState

    private val _deleteHouseState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteHouseState: StateFlow<UiState<Any>> = _deleteHouseState

    private val _updateHouseServicesState = MutableStateFlow<UiState<House>>(UiState.Empty)
    val updateHouseServicesState: StateFlow<UiState<House>> = _updateHouseServicesState

    private val _houseState = MutableStateFlow<UiState<House>>(UiState.Loading)
    val houseState: StateFlow<UiState<House>> = _houseState

    private val _createServiceRecordState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createServiceRecordState: StateFlow<UiState<Any>> = _createServiceRecordState

    private val _serviceRecordsState = MutableStateFlow<UiState<List<ServiceRecord>>>(UiState.Empty)
    val serviceRecordsState: StateFlow<UiState<List<ServiceRecord>>> = _serviceRecordsState

    private val _expenseRecordsState = MutableStateFlow<UiState<List<ExpenseRecord>>>(UiState.Empty)
    val expenseRecordsState: StateFlow<UiState<List<ExpenseRecord>>> = _expenseRecordsState

    private val _createExpenseRecordState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val createExpenseRecordState: StateFlow<UiState<Any>> = _createExpenseRecordState

    private val _financialReportAllMonth = MutableStateFlow<UiState<List<FinancialReport>>>(UiState.Empty)
    val financialReportAllMonth: StateFlow<UiState<List<FinancialReport>>> = _financialReportAllMonth

    private val _deleteExpenseRecordState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val deleteExpenseRecordState: StateFlow<UiState<Any>> = _deleteExpenseRecordState

    var selectedHouse by mutableStateOf<House?>(null)

    fun updateHouseServices(
        houseId: String,
        rentService: Service? = null,
        electricService: Service? = null,
        waterService: Service? = null,
        billingDay: Int? = null
    ) {
        viewModelScope.launch {
            _updateHouseServicesState.value = UiState.Loading
            _updateHouseServicesState.value = houseRepository.updateHouseServices(
                houseId = houseId,
                rentService = rentService,
                electricService = electricService,
                waterService = waterService,
                billingDay = billingDay
            )
        }
    }

    fun getHouseById(houseId: String) {
        _houseState.value = UiState.Loading
        viewModelScope.launch {
            _houseState.value = houseRepository.getHouseById(houseId)
        }
    }

    fun addAndUpdateHouse(house: House) {
        _createHouseState.value = UiState.Loading
        viewModelScope.launch {
            _createHouseState.value = houseRepository.addAndUpdateHouse(house)
        }
    }

    fun createServiceRecord(serviceRecord: ServiceRecord) {
        _createServiceRecordState.value = UiState.Loading
        viewModelScope.launch {
            _createServiceRecordState.value = houseRepository.createServiceRecord(serviceRecord)
        }
    }

    fun fetchServiceRecords(houseId: String) {
        _serviceRecordsState.value = UiState.Loading
        viewModelScope.launch {
            _serviceRecordsState.value = houseRepository.getServiceRecordsByHouseId(houseId)
        }
    }

    fun clearCreateExpenseRecordState() {
        _createExpenseRecordState.value = UiState.Empty
    }

    fun fetchHouses(uid: String) {
        _housesState.value = UiState.Loading
        viewModelScope.launch {
            _housesState.value = houseRepository.getHouses(uid)
        }
    }

    fun deleteHouse(houseId: String) {
        viewModelScope.launch {
            _housesState.value = UiState.Loading
            _deleteHouseState.value = UiState.Loading
            _deleteHouseState.value = houseRepository.deleteHouse(houseId)
        }
    }

    fun createExpenseRecord(expenseRecord: ExpenseRecord){
        _createExpenseRecordState.value = UiState.Loading
        viewModelScope.launch {
            _createExpenseRecordState.value = houseRepository.createExpenseRecord(expenseRecord)
        }
    }

    fun fetchExpenseRecords(houseId: String) {
        _expenseRecordsState.value = UiState.Loading
        viewModelScope.launch {
            _expenseRecordsState.value = houseRepository.getExpenseRecords(houseId)
        }
    }

    fun deleteExpenseRecord(expenseRecordId: String) {
        _deleteExpenseRecordState.value = UiState.Loading
        viewModelScope.launch {
            _deleteExpenseRecordState.value = houseRepository.deleteExpenseRecord(expenseRecordId)
        }
    }

    fun fetchFinancialReportAllMonth(houseId: String, year: Int) {
        _financialReportAllMonth.value = UiState.Loading
        viewModelScope.launch {
            _financialReportAllMonth.value = houseRepository.getFinancialReportForAllMonths(houseId = houseId, year = year)
        }
    }

    fun clearCreateHouseState() {
        _createHouseState.value = UiState.Empty
    }

    fun clearDeleteHouseState() {
        _deleteHouseState.value = UiState.Empty
    }

    fun clearDeleteExpenseRecordState() {
        _deleteExpenseRecordState.value = UiState.Empty
    }

    fun clearDeleteState() {
        _deleteHouseState.value = UiState.Empty
    }

    fun clearCreateServiceRecordState() {
        _createServiceRecordState.value = UiState.Empty
    }
}