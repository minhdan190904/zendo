package com.gig.zendo.ui.presentation.room

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.common.FunctionIcon
import com.gig.zendo.ui.common.HomeSearchBar
import com.gig.zendo.ui.common.RoomFilterState
import com.gig.zendo.ui.common.RoomSortOption
import com.gig.zendo.ui.common.RoomSortState
import com.gig.zendo.ui.common.SortFilterBottomSheet
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.NavArgUtil
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getToday
import timber.log.Timber


data class HomeAction(
    @DrawableRes val iconRes: Int,
    val label: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    navController: NavController,
    viewModel: RoomViewModel,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    houseName: String
) {
    val actions = listOf(
        HomeAction(
            R.drawable.ic_add,
            "Thêm phòng"
        ) { navController.navigate(Screens.CreateRoomScreen.route + "/${houseId}") },
        HomeAction(R.drawable.ic_lightbub, "Ghi điện nước") {
            navController.navigate(Screens.ServiceRecordScreen.route + "/${houseId}")
        },
        HomeAction(
            R.drawable.ic_money,
            "Thu tiền"
        ) { navController.navigate(Screens.AcceptInvoiceScreen.route + "/${houseId}") },
        HomeAction(
            R.drawable.ic_setting,
            "Cài đặt"
        ) { navController.navigate(Screens.ServiceScreen.route + "/${houseId}") },
        HomeAction(
            R.drawable.ic_guide,
            "Hướng dẫn"
        ) { navController.navigate(Screens.InstructionScreen.route) }
    )

    val roomsState by viewModel.roomsState.collectAsStateWithLifecycle()
    val checkOutTenantState by viewModel.checkOutTenantState.collectAsStateWithLifecycle()
    var tenantIdState by remember {
        mutableStateOf<String?>(null)
    }

    var showBottomDialog by remember {
        mutableStateOf(false)
    }

    val shouldRefresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("shouldRefreshRooms") == true || navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>(
        "shouldRefreshRooms"
    ) == true

    var roomSortState by remember { mutableStateOf(RoomSortState()) }
    var roomFilterState by remember { mutableStateOf(RoomFilterState()) }

    LaunchedEffect(Unit) {
        if (shouldRefresh || roomsState !is UiState.Success) {
            viewModel.fetchRoomsWithTenants(houseId)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("shouldRefreshRooms", false)
            navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefreshRooms", false)
        }
    }

    LaunchedEffect(checkOutTenantState) {
        when (checkOutTenantState) {
            is UiState.Success -> {
                viewModel.clearCheckOutTenantState()
                viewModel.fetchRoomsWithTenants(houseId)
                snackbarHostState.showSnackbar("✓ Trả phòng thành công")
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar(
                    "✗ " + (checkOutTenantState as UiState.Failure).error
                )
            }

            else -> {}
        }
    }


    tenantIdState?.let { tenantId ->
        ConfirmDialog(
            title = "Trả phòng",
            message = "Bạn có muốn trả phòng này không?",
            onConfirm = {
                val tenantIdTemp = tenantId
                viewModel.checkOutTenant(tenantIdTemp, getToday())
                tenantIdState = null
            },
            onDismiss = { tenantIdState = null },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(houseName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ActionMenuRow(actions = actions)
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = if (roomsState is UiState.Success) Alignment.TopCenter else Alignment.Center
            ) {
                when (roomsState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = DarkGreen
                        )
                    }

                    is UiState.Failure -> {
                        Text(
                            text = "Lỗi tải dữ liệu",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    is UiState.Empty -> {
                        EmptyStateMessage()
                    }

                    else -> {
                        val rooms =
                            (roomsState as UiState.Success<List<Pair<Room, List<Tenant>>>>).data

                        Column {

                            HomeSearchBar(
                                onFilterClick = {
                                    showBottomDialog = true
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //filter by roomFilterState
//                            data class RoomFilterState(
//                                val roomNotEmpty : Boolean = false,
//                                val roomHaveOutstandingAmount: Boolean = false
//                            )
                            var filteredRooms = rooms

                            if(roomFilterState.roomNotEmpty) {
                                filteredRooms = filteredRooms.filter { !it.first.empty }
                            }

                            if(roomFilterState.roomHaveOutstandingAmount) {
                                filteredRooms = filteredRooms.filter { it.first.outstandingAmount > 0}
                            }

                            when (roomSortState.option) {
                                RoomSortOption.Name -> {
                                    filteredRooms = if(roomSortState.ascending) {
                                        filteredRooms.sortedBy { it.first.name }
                                    } else {
                                        filteredRooms.sortedByDescending { it.first.name }
                                    }
                                }

                                RoomSortOption.TimeCreated -> {
                                    filteredRooms = if(roomSortState.ascending) {
                                        filteredRooms.sortedBy { it.first.createdAt }
                                    } else {
                                        filteredRooms.sortedByDescending { it.first.createdAt }
                                    }
                                }
                                RoomSortOption.RoomNotEmpty -> {
                                    filteredRooms = if(roomSortState.ascending) {
                                        filteredRooms.sortedBy { it.first.empty }
                                    } else {
                                        filteredRooms.sortedByDescending { it.first.empty }
                                    }
                                }

                                RoomSortOption.OutstandingAmount -> {
                                    filteredRooms = if(roomSortState.ascending) {
                                        filteredRooms.sortedBy { it.first.outstandingAmount }
                                    } else {
                                        filteredRooms.sortedByDescending { it.first.outstandingAmount }
                                    }
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(filteredRooms) { room ->

                                    val tenant = room.second.firstOrNull { it.active }

                                    if(tenant != null){
                                        Timber.tag("RoomScreen").d("Tenant found: $tenant in room ${room.first.name}")
                                    }

                                    PropertyRoomCard(
                                        room = room.first,
                                        tenant = tenant,
                                        onCreateInvoice = {
                                            viewModel.updateRoomAndTenantCurrent(
                                                Pair(
                                                    room.first,
                                                    tenant ?: Tenant()
                                                )
                                            )
                                            navController.navigate(Screens.CreateInvoiceScreen.route + "/${houseId}")
                                        },
                                        onAddTenant = {
                                            navController.navigate(Screens.CreateTenantScreen.route + "/${room.first.id}/${houseId}")
                                        },
                                        onCheckHistory = {
                                            navController.navigate(Screens.TenantHistoryScreen.route + "/${room.first.id}")
                                        },
                                        onCheckOut = {
                                            tenantIdState = tenant?.id
                                        },
                                        onCheckDetail = {
                                            val tenantJson = NavArgUtil.encode(tenant)
                                            navController.navigate(Screens.TenantDetailScreen.route + "/${tenantJson}/${room.first.name}")
                                        },
                                        onCheckAllInvoices = {
                                            navController.navigate(Screens.InvoiceHistoryScreen.route + "/${room.first.id}")
                                        },
                                        onEditRoom = {
                                            viewModel.selectedRoom = room.first
                                            navController.navigate(Screens.CreateRoomScreen.route + "/${houseId}")
                                        },
                                        onEditTenant = {
                                            viewModel.selectedTenant = tenant
                                            navController.navigate(Screens.CreateTenantScreen.route + "/${room.first.id}/${houseId}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    SortFilterBottomSheet(
        visible = showBottomDialog,
        roomSortState = roomSortState,
        filters = roomFilterState,
        onDismiss = {  showBottomDialog = false },
        onApply = { sort, filter ->
            roomSortState = sort
            roomFilterState = filter
            showBottomDialog = false
        },
        onReset = {}
    )
}

@Composable
fun ActionMenuRow(
    actions: List<HomeAction>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        actions.forEach { action ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = action.onClick
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FunctionIcon(iconRes = action.iconRes, contentDescription = action.label)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = action.label,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun EmptyStateMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nhà trọ chưa được tạo phòng.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Đầu tiên hãy vào “cài đặt” để thiết lập các mặc định.\n" +
                    "Sau đó hãy tạo phòng mới",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
