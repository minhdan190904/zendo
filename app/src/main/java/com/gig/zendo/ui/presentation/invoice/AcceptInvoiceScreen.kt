package com.gig.zendo.ui.presentation.invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.domain.model.Room
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.ExposedDropdownField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.utils.NavArgUtil
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getFirstDayOfThisMonth
import com.gig.zendo.utils.getToday
import com.gig.zendo.utils.toDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcceptInvoiceScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    viewModelRoom: RoomViewModel,
    viewModelInvoice: InvoiceViewModel,
) {
    var selectedDateStart by remember { mutableStateOf(getFirstDayOfThisMonth()) }
    var selectedDateEnd by remember { mutableStateOf(getToday()) }
    var selectedRoomId by remember { mutableStateOf("") }

    val invoicesState by viewModelInvoice.invoicesStateInHouse.collectAsStateWithLifecycle()
    val roomsState by viewModelRoom.roomsState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val checkedMapNotPaidInvoiceId = remember { mutableStateMapOf<String, Boolean>() }
    val updateStatusPaidState by viewModelInvoice.updateStatusPaidState.collectAsStateWithLifecycle()

    var sortField by remember { mutableStateOf(SortFieldInvoice.DATE) }
    var isAscending by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModelInvoice.getInvoicesInHouse(houseId)
    }

    LaunchedEffect(updateStatusPaidState) {
        when (updateStatusPaidState) {
            is UiState.Loading -> {
                // Show loading state if needed
            }

            is UiState.Success -> {
                viewModelInvoice.getInvoicesInHouse(houseId)
                checkedMapNotPaidInvoiceId.clear()
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                snackbarHostState.showSnackbar("Đã cập nhật hoá đơn thành công")
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar("Lỗi: ${(updateStatusPaidState as UiState.Failure).error}")
            }

            is UiState.Empty -> {
                // Handle empty state if needed
            }
        }
    }

    // Làm sạch checkedMapNotPaidInvoiceId khi danh sách hóa đơn thay đổi
    LaunchedEffect(invoicesState, selectedRoomId, selectedDateStart, selectedDateEnd, sortField, isAscending) {
        if (invoicesState is UiState.Success) {
            val filteredByRoom = if (selectedRoomId.isEmpty()) {
                (invoicesState as UiState.Success).data
            } else {
                (invoicesState as UiState.Success).data.filter { it.roomId == selectedRoomId }
            }

            val filteredInvoices = filteredByRoom.filter {
                val date = it.date.toDate()
                date >= selectedDateStart.toDate() && date <= selectedDateEnd.toDate()
            }

            val sortedInvoices = when (sortField) {
                SortFieldInvoice.DATE -> if (isAscending) filteredInvoices.sortedWith(
                    compareBy<Invoice> { it.date.toDate() }.thenBy { it.createdAt }
                ) else filteredInvoices.sortedWith(
                    compareByDescending<Invoice> { it.date.toDate() }.thenBy { it.createdAt }
                )
                SortFieldInvoice.ROOM -> if (isAscending) filteredInvoices.sortedBy { it.roomName } else filteredInvoices.sortedByDescending { it.roomName }
                SortFieldInvoice.TOTAL -> if (isAscending) filteredInvoices.sortedBy { it.totalAmount } else filteredInvoices.sortedByDescending { it.totalAmount }
                SortFieldInvoice.STATUS -> if (isAscending) filteredInvoices.sortedBy { it.status } else filteredInvoices.sortedByDescending { it.status }
            }

            val currentInvoiceIds = sortedInvoices.map { it.id }.toSet()
            checkedMapNotPaidInvoiceId.keys.retainAll { it in currentInvoiceIds }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thu tiền") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Danh sách hoá đơn",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold, fontSize = 24.sp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )

                Spacer(Modifier.height(12.dp))

                Column {
                    CustomDateTimePicker(
                        date = selectedDateStart,
                        onDateChange = { selectedDateStart = it },
                        label = "Chọn ngày bắt đầu"
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomDateTimePicker(
                        date = selectedDateEnd,
                        onDateChange = { selectedDateEnd = it },
                        label = "Chọn ngày kết thúc"
                    )

                    Spacer(Modifier.height(16.dp))

                    if (roomsState is UiState.Success) {
                        val rooms = (roomsState as UiState.Success).data.map { it.first }
                        val roomOptions = getRoomOptions(rooms)
                        ExposedDropdownField(
                            label = "Chọn phòng",
                            options = roomOptions,
                            selectedOption = selectedRoomId,
                            onOptionSelected = { selectedRoomId = it },
                            labelMapper = {
                                if (it.isEmpty()) "Tất cả" else rooms.find { room -> room.id == it }?.name
                                    ?: it
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                when (val state = invoicesState) {
                    is UiState.Loading -> {
                        CustomLoadingProgress()
                    }

                    is UiState.Success -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SortableHeaderText(
                                text = "Ngày",
                                active = sortField == SortFieldInvoice.DATE,
                                isAscending = isAscending,
                                onClick = {
                                    if (sortField == SortFieldInvoice.DATE) {
                                        isAscending = !isAscending
                                    } else {
                                        sortField = SortFieldInvoice.DATE
                                        isAscending = true
                                    }
                                },
                                weight = 2f
                            )

                            SortableHeaderText(
                                text = "Phòng",
                                active = sortField == SortFieldInvoice.ROOM,
                                isAscending = isAscending,
                                onClick = {
                                    if (sortField == SortFieldInvoice.ROOM) {
                                        isAscending = !isAscending
                                    } else {
                                        sortField = SortFieldInvoice.ROOM
                                        isAscending = true
                                    }
                                },
                                weight = 2f
                            )

                            SortableHeaderText(
                                text = "Tổng tiền",
                                active = sortField == SortFieldInvoice.TOTAL,
                                isAscending = isAscending,
                                onClick = {
                                    if (sortField == SortFieldInvoice.TOTAL) {
                                        isAscending = !isAscending
                                    } else {
                                        sortField = SortFieldInvoice.TOTAL
                                        isAscending = true
                                    }
                                },
                                weight = 2f
                            )

                            SortableHeaderText(
                                text = "Đóng",
                                active = sortField == SortFieldInvoice.STATUS,
                                isAscending = isAscending,
                                onClick = {
                                    if (sortField == SortFieldInvoice.STATUS) {
                                        isAscending = !isAscending
                                    } else {
                                        sortField = SortFieldInvoice.STATUS
                                        isAscending = true
                                    }
                                },
                                weight = 1.5f
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        val filteredByRoom = if (selectedRoomId.isEmpty()) {
                            state.data
                        } else {
                            state.data.filter { it.roomId == selectedRoomId }
                        }

                        val filteredInvoices = filteredByRoom.filter {
                            val date = it.date.toDate()
                            date >= selectedDateStart.toDate() && date <= selectedDateEnd.toDate()
                        }

                        val sortedInvoices = when (sortField) {
                            SortFieldInvoice.DATE -> if (isAscending) filteredInvoices.sortedWith(
                                compareBy<Invoice> { it.date.toDate() }.thenBy { it.createdAt }
                            ) else filteredInvoices.sortedWith(
                                compareByDescending<Invoice> { it.date.toDate() }.thenBy { it.createdAt }
                            )
                            SortFieldInvoice.ROOM -> if (isAscending) filteredInvoices.sortedBy { it.roomName } else filteredInvoices.sortedByDescending { it.roomName }
                            SortFieldInvoice.TOTAL -> if (isAscending) filteredInvoices.sortedBy { it.totalAmount } else filteredInvoices.sortedByDescending { it.totalAmount }
                            SortFieldInvoice.STATUS -> if (isAscending) filteredInvoices.sortedBy { it.status } else filteredInvoices.sortedByDescending { it.status }
                        }

                        if(roomsState is UiState.Success) {

                            val roomsAndTenants = (roomsState as UiState.Success).data
                            val rooms = roomsAndTenants.map { it.first }
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(
                                    items = sortedInvoices,
                                    key = { invoice -> invoice.id }
                                ) { invoice ->

                                    val roomNameUpdate = rooms.find { room -> room.id == invoice.roomId }?.name ?: ""
                                    val tenantUpdate = roomsAndTenants.find { it.first.id == invoice.roomId }?.second?.firstOrNull()
                                    val invoiceUpdate = invoice.copy(
                                        roomName = roomNameUpdate,
                                        tenant = tenantUpdate ?: invoice.tenant
                                    )

                                    InvoiceItemRow(
                                        invoice = invoiceUpdate,
                                        onViewClick = {
                                            val invoiceJson = NavArgUtil.encode(invoiceUpdate)
                                            navController.navigate(Screens.InvoiceDetailScreen.route + "/${invoiceJson}")
                                        },
                                        onCheckedChange = { isChecked ->
                                            if (invoice.status == InvoiceStatus.NOT_PAID) {
                                                checkedMapNotPaidInvoiceId[invoice.id] = isChecked
                                            }
                                        },
                                        checkedMap = checkedMapNotPaidInvoiceId
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        CustomElevatedButton(
                            onClick = {
                                val listInvoiceNotPaidIdSelected =
                                    checkedMapNotPaidInvoiceId.filter { it.value }.keys.toList()
                                if (listInvoiceNotPaidIdSelected.isEmpty()) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Vui lòng chọn ít nhất một hoá đơn chưa thanh toán")
                                    }
                                } else {
                                    viewModelInvoice.updateStatusPaidForInvoices(
                                        listInvoiceNotPaidIdSelected
                                    )
                                }
                            },
                            text = "Xác nhận đóng tiền"
                        )
                    }

                    is UiState.Failure -> {
                        Text("Error: ${state.error}", color = Color.Red)
                    }

                    is UiState.Empty -> {
                        Text("Không có dữ liệu", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = updateStatusPaidState is UiState.Loading)
}

@Composable
fun RowScope.SortableHeaderText(
    text: String,
    active: Boolean,
    isAscending: Boolean,
    onClick: () -> Unit,
    weight: Float = 1f
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .weight(weight)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        if (active) {
            Icon(
                painter = if (isAscending) {
                    painterResource(id = R.drawable.ic_arrow_upward)
                } else {
                    painterResource(id = R.drawable.ic_arrow_downward)
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Black
            )
        }
    }
}

enum class SortFieldInvoice {
    DATE, ROOM, TOTAL, STATUS
}

private fun getRoomOptions(rooms: List<Room>): List<String> {
    rooms.sortedBy { it.name }
    return listOf("") + rooms.map { it.id }
}