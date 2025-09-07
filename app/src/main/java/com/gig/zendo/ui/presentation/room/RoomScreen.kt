package com.gig.zendo.ui.presentation.room

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.common.HomeSearchBar
import com.gig.zendo.ui.common.RoomFilterState
import com.gig.zendo.ui.common.RoomSortOption
import com.gig.zendo.ui.common.RoomSortState
import com.gig.zendo.ui.common.SortFilterBottomSheet
import com.gig.zendo.ui.common.ZendoScaffold
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.NavArgUtil
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getToday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    navController: NavController,
    viewModel: RoomViewModel,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    houseName: String
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val roomsState by viewModel.roomsState.collectAsStateWithLifecycle()
    val checkOutTenantState by viewModel.checkOutTenantState.collectAsStateWithLifecycle()
    var tenantIdState by remember { mutableStateOf<String?>(null) }

    var showBottomDialog by remember { mutableStateOf(false) }
    var roomSortState by remember { mutableStateOf(RoomSortState()) }
    var roomFilterState by remember { mutableStateOf(RoomFilterState()) }

    val shouldRefresh =
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshRooms") == true ||
                navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshRooms") == true

    LaunchedEffect(Unit) {
        if (shouldRefresh || roomsState !is UiState.Success) {
            viewModel.fetchRoomsWithTenants(houseId)
            navController.currentBackStackEntry?.savedStateHandle?.set("shouldRefreshRooms", false)
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

            is UiState.Failure -> snackbarHostState.showSnackbar("✗ " + (checkOutTenantState as UiState.Failure).error)
            else -> Unit
        }
    }

    tenantIdState?.let { tenantId ->
        ConfirmDialog(
            title = "Trả phòng",
            message = "Bạn có muốn trả phòng này không?",
            onConfirm = {
                viewModel.checkOutTenant(tenantId, getToday())
                tenantIdState = null
            },
            onDismiss = { tenantIdState = null }
        )
    }

    val actions = listOf(
        HomeAction(R.drawable.ic_lightbub, "Ghi điện nước") {
            navController.navigate(Screens.ServiceRecordScreen.route + "/$houseId")
        },
        HomeAction(R.drawable.ic_money, "Thu tiền") {
            navController.navigate(Screens.AcceptInvoiceScreen.route + "/$houseId")
        },
        HomeAction(R.drawable.ic_setting, "Cài đặt") {
            navController.navigate(Screens.ServiceScreen.route + "/$houseId")
        }
    )

    ZendoScaffold(
        title = houseName,
        onBack = { navController.popBackStack() },
        topBarContainerColor = Color.Transparent,
        actions = {
            RoomTopActionsMenu(actions = actions)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screens.CreateRoomScreen.route + "/$houseId") },
                icon = { Icon(painterResource(R.drawable.ic_add), null) },
                text = { Text("Thêm phòng") },
                containerColor = colorScheme.secondary,
                contentColor = colorScheme.onSecondary
            )

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (roomsState is UiState.Success) Alignment.TopCenter else Alignment.Center
            ) {
                when (roomsState) {
                    is UiState.Loading -> CircularProgressIndicator(
                        Modifier.size(48.dp),
                        colorScheme.primary
                    )

                    is UiState.Failure -> Text(
                        "Lỗi tải dữ liệu",
                        color = colorScheme.error,
                        style = typography.bodyMedium
                    )

                    is UiState.Empty -> EmptyStateMessage()
                    else -> {
                        val rooms =
                            (roomsState as UiState.Success<List<Pair<Room, List<Tenant>>>>).data

                        Column {
                            HomeSearchBar(onFilterClick = { showBottomDialog = true })
                            Spacer(Modifier.height(8.dp))

                            var filteredRooms = rooms
                            if (roomFilterState.roomNotEmpty) filteredRooms =
                                filteredRooms.filter { !it.first.empty }
                            if (roomFilterState.roomHaveOutstandingAmount) filteredRooms =
                                filteredRooms.filter { it.first.outstandingAmount > 0 }

                            when (roomSortState.option) {
                                RoomSortOption.Name -> filteredRooms =
                                    if (roomSortState.ascending) filteredRooms.sortedBy { it.first.name }
                                    else filteredRooms.sortedByDescending { it.first.name }

                                RoomSortOption.TimeCreated -> filteredRooms =
                                    if (roomSortState.ascending) filteredRooms.sortedBy { it.first.createdAt }
                                    else filteredRooms.sortedByDescending { it.first.createdAt }

                                RoomSortOption.RoomNotEmpty -> filteredRooms =
                                    if (roomSortState.ascending) filteredRooms.sortedBy { it.first.empty }
                                    else filteredRooms.sortedByDescending { it.first.empty }

                                RoomSortOption.OutstandingAmount -> filteredRooms =
                                    if (roomSortState.ascending) filteredRooms.sortedBy { it.first.outstandingAmount }
                                    else filteredRooms.sortedByDescending { it.first.outstandingAmount }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp
                                )
                            ) {
                                items(filteredRooms) { room ->
                                    val tenant = room.second.firstOrNull { it.active }

                                    PropertyRoomCard(
                                        room = room.first,
                                        tenant = tenant,
                                        onCreateInvoice = {
                                            viewModel.updateRoomAndTenantCurrent(
                                                room.first to (tenant ?: Tenant())
                                            )
                                            navController.navigate(Screens.CreateInvoiceScreen.route + "/$houseId")
                                        },
                                        onAddTenant = {
                                            navController.navigate(Screens.CreateTenantScreen.route + "/${room.first.id}/$houseId")
                                        },
                                        onCheckHistory = {
                                            navController.navigate(Screens.TenantHistoryScreen.route + "/${room.first.id}")
                                        },
                                        onCheckOut = { tenantIdState = tenant?.id },
                                        onCheckDetail = {
                                            val tenantJson = NavArgUtil.encode(tenant)
                                            navController.navigate(Screens.TenantDetailScreen.route + "/$tenantJson/${room.first.name}")
                                        },
                                        onCheckAllInvoices = {
                                            navController.navigate(Screens.InvoiceHistoryScreen.route + "/${room.first.id}")
                                        },
                                        onEditRoom = {
                                            viewModel.selectedRoom = room.first
                                            navController.navigate(Screens.CreateRoomScreen.route + "/$houseId")
                                        },
//                                        onEditTenant = {
//                                            viewModel.selectedTenant = tenant
//                                            navController.navigate(Screens.CreateTenantScreen.route + "/${room.first.id}/$houseId")
//                                        }
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
        onDismiss = { showBottomDialog = false },
        onApply = { sort, filter ->
            roomSortState = sort
            roomFilterState = filter
            showBottomDialog = false
        },
        onReset = {}
    )
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
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Đầu tiên hãy vào “cài đặt” để thiết lập các mặc định.\n" +
                    "Sau đó hãy tạo phòng mới",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

data class HomeAction(
    val iconRes: Int,
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun RoomTopActionsMenu(
    actions: List<HomeAction>
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(R.drawable.ic_more_vert),
            contentDescription = "More"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        offset = DpOffset(x = (-16).dp, y = 0.dp)
    ) {
        actions.forEach { action ->
            DropdownMenuItem(
                text = { Text(action.label) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = action.iconRes),
                        contentDescription = null
                    )
                },
                onClick = {
                    expanded = false
                    action.onClick()
                }
            )
        }
    }
}


