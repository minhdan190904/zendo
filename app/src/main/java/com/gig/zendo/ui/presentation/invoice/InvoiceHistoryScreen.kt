package com.gig.zendo.ui.presentation.invoice


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.utils.NavArgUtil
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.toMoney


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceHistoryScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    roomId: String,
    viewModel: InvoiceViewModel,
    viewModelRoom: RoomViewModel
) {

    val invoicesState by viewModel.invoicesStateInRoom.collectAsStateWithLifecycle()
    val roomsState by viewModelRoom.roomsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getInvoicesInRoom(roomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách hóa đơn") },
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
                .padding(horizontal = 16.dp),
            contentAlignment = if (invoicesState is UiState.Success) Alignment.TopCenter else Alignment.Center
        ) {
            when (invoicesState) {
                is UiState.Loading -> {
                    CustomLoadingProgress()
                }

                is UiState.Success -> {
                    val invoices = (invoicesState as UiState.Success).data
                    if (invoices.isEmpty()) {
                        Text("Không có hóa đơn nào", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        if(roomsState is UiState.Success) {
                            val roomsAndTenants = (roomsState as UiState.Success).data
                            val rooms = roomsAndTenants.map { it.first }
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(invoices.size) { index ->
                                val invoice = invoices[index]
                                val roomNameUpdate = rooms.find { room -> room.id == invoice.roomId }?.name ?: ""
                                val tenantUpdate = roomsAndTenants.find { it.first.id == invoice.roomId }?.second?.firstOrNull()
                                val invoiceUpdate = invoice.copy(
                                    roomName = roomNameUpdate,
                                    tenant = tenantUpdate ?: invoice.tenant
                                )
                                PaymentItem(
                                    date = invoice.date,
                                    amount = invoice.totalAmount.toMoney(),
                                    statusInvoice = invoice.status,
                                    onDetailClick = {
                                        val invoiceJson = NavArgUtil.encode(invoiceUpdate)
                                        navController.navigate(Screens.InvoiceDetailScreen.route + "/${invoiceJson}")
                                    },
                                    onDeleteClick = {
                                        // Handle delete click
                                    }
                                )
                            }
                            }
                        }
                    }
                }

                is UiState.Failure -> {
                    Text("Lỗi: ${(invoicesState as UiState.Failure).error}", color = Color.Red)
                }

                UiState.Empty -> {
                    Text("Không có dữ liệu", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

}