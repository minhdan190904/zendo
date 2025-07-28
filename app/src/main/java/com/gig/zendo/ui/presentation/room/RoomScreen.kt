package com.gig.zendo.ui.presentation.room

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import com.gig.zendo.ui.common.FunctionIcon
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.presentation.tenant.getToday


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
        HomeAction(R.drawable.ic_lightbub, "Ghi điện nước") { /* onRecord() */ },
        HomeAction(R.drawable.ic_money, "Thu tiền") {  },
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
    var tenantIdState by remember {
        mutableStateOf<String?>(null)
    }

    val shouldRefresh = navController.currentBackStackEntry
            ?.savedStateHandle
        ?.get<Boolean>("shouldRefreshRooms") == true || navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshRooms") == true

    LaunchedEffect(Unit) {
        if (shouldRefresh || roomsState !is UiState.Success) {
            viewModel.fetchRoomsWithTenants(houseId)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("shouldRefreshRooms", false)
            navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefreshRooms", false)
        }
    }


    tenantIdState?.let { tenantId ->
        ConfirmDialog(
            title = "Trả phòng",
            message = "Bạn có muốn trả phòng này không?",
            onConfirm = {
                Log.i("RoomScreen", "Checking out tenant with ID: $tenantId")
                viewModel.checkOutTenant(tenantId, getToday())
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
                if (roomsState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = DarkGreen
                    )
                } else if (roomsState is UiState.Failure) {
                    Text(
                        text = "Lỗi tải dữ liệu",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                } else if (roomsState is UiState.Empty) {
                    EmptyStateMessage()
                } else {
                    val rooms = (roomsState as UiState.Success<List<Pair<Room, List<Tenant>>>>).data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(rooms) { room ->

                            val tenant = room.second.firstOrNull { it.active == true }

                            PropertyRoomCard(
                                room = room.first,
                                tenant = tenant,
                                onCreateInvoice = {
                                    navController.navigate(Screens.CreateInvoiceScreen.route + "/${room.first.id}/${houseId}")
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
                                onCheckDetail ={
                                    navController.navigate(Screens.TenantDetailScreen.route + "/${room.first.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
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
