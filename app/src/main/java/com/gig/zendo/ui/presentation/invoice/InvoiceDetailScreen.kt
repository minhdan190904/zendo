package com.gig.zendo.ui.presentation.invoice

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomSwitch
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.presentation.tenant.StatOfDetailImage
import com.gig.zendo.ui.presentation.tenant.StatOfDetailText
import com.gig.zendo.ui.presentation.tenant.StatOfDetailTextHeader
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.toMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: InvoiceViewModel,
    invoiceId: String
) {
    val invoicesStateInRoom by viewModel.invoicesStateInRoom.collectAsStateWithLifecycle()
    val invoicesStateInHouse by viewModel.invoicesStateInHouse.collectAsStateWithLifecycle()
    val invoiceCurrentCreated by viewModel.createInvoiceState.collectAsStateWithLifecycle()

    val invoiceInRoom = if (invoicesStateInRoom is UiState.Success) {
        (invoicesStateInRoom as UiState.Success).data.firstOrNull { it.id == invoiceId }
    } else null

    val invoiceInHouse = if (invoicesStateInHouse is UiState.Success) {
        (invoicesStateInHouse as UiState.Success).data.firstOrNull { it.id == invoiceId }
    } else null

    val invoiceCurrent = if (invoiceCurrentCreated is UiState.Success) {
        (invoiceCurrentCreated as UiState.Success).data
    } else null

    val invoice = invoiceInRoom ?: invoiceInHouse ?: invoiceCurrent

    var invoiceStatusChecked by remember {
        mutableStateOf(
            invoice?.status ?: InvoiceStatus.NOT_PAID
        )
    }

    val updateInvoiceStatusState by viewModel.updateStatusInvoiceState.collectAsStateWithLifecycle()

    LaunchedEffect(updateInvoiceStatusState) {
        when (val state = updateInvoiceStatusState) {
            is UiState.Success -> {
                navController.popBackStack()
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                navController.popBackStack()
                viewModel.resetStateUpdateStatus()
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar(
                    state.error ?: "Cập nhật trạng thái hóa đơn thất bại"
                )
            }

            else -> {}
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetStateCreateInvoice()
                        navController.popBackStack()
                    }) {
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFA598))
                                .padding(horizontal = 16.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = SpaceBetween
                            ) {
                                Text(
                                    text = invoice?.roomName ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                    color = Color.Black
                                )

                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Xóa",
                                        tint = Color.Black,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (invoice != null) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    StatOfDetailText(
                                        label = "Ngày lập hóa đơn",
                                        value = invoice.date
                                    )

                                    StatOfDetailTextHeader(label = "Thông tin khách thuê")

                                    StatOfDetailText(
                                        label = "Họ tên",
                                        value = invoice.tenant.name
                                    )
                                    StatOfDetailText(
                                        label = "Số điện thoại",
                                        value = invoice.tenant.phone
                                    )
                                    StatOfDetailText(
                                        label = "Số người ở",
                                        value = invoice.tenant.numberOfOccupants.toString()
                                    )

                                    StatOfDetailTextHeader(label = "Thông tin điện nước")

                                    StatOfDetailTextHeader(label = "Nước")

                                    if (invoice.waterService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                        StatOfDetailText(
                                            label = "Số nước cũ",
                                            value = invoice.oldNumberWater.toString()
                                        )
                                        StatOfDetailText(
                                            label = "Số nước mới",
                                            value = invoice.newNumberWater.toString()
                                        )
                                        StatOfDetailText(
                                            label = "Số nước tiêu thụ",
                                            value = (invoice.newNumberWater - invoice.oldNumberWater).toString()
                                        )
                                        StatOfDetailText(
                                            label = "Giá nước",
                                            value = invoice.waterService.chargeValue.toMoney()
                                        )
                                        StatOfDetailText(
                                            label = "Tiền nước",
                                            value = ((invoice.newNumberWater - invoice.oldNumberWater) * invoice.waterService.chargeValue).toMoney(),
                                            color = DarkGreen
                                        )
                                        if (invoice.oldWaterImageUrl.isNotEmpty()) {
                                            StatOfDetailImage(
                                                label = "Ảnh số nước cũ",
                                                value = invoice.oldWaterImageUrl,
                                            )
                                        } else {
                                            StatOfDetailText(
                                                label = "Ảnh số nước cũ",
                                                value = invoice.oldWaterImageUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                        if (invoice.newWaterImageUrl.isNotEmpty()) {
                                            StatOfDetailImage(
                                                label = "Ảnh số nước mới",
                                                value = invoice.newWaterImageUrl,
                                            )
                                        } else {
                                            StatOfDetailText(
                                                label = "Ảnh số nước mới",
                                                value = invoice.newWaterImageUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                    } else {
                                        StatOfDetailText(
                                            label = "Giá nước x 1 người",
                                            value = invoice.waterService.chargeValue.toMoney()
                                        )
                                        StatOfDetailText(
                                            label = "Tiền nước x " + invoice.tenant.numberOfOccupants.toString() + " người",
                                            value = (invoice.tenant.numberOfOccupants * invoice.waterService.chargeValue).toMoney(),
                                            color = DarkGreen
                                        )
                                    }

                                    StatOfDetailTextHeader(label = "Điện")

                                    if (invoice.electricService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                        StatOfDetailText(
                                            label = "Số điện cũ",
                                            value = invoice.oldNumberElectric.toString()
                                        )
                                        StatOfDetailText(
                                            label = "Số điện mới",
                                            value = invoice.newNumberElectric.toString()
                                        )
                                        StatOfDetailText(
                                            label = "Số điện tiêu thụ",
                                            value = (invoice.newNumberElectric - invoice.oldNumberElectric).toString()
                                        )
                                        StatOfDetailText(
                                            label = "Giá điện",
                                            value = invoice.electricService.chargeValue.toMoney()
                                        )
                                        StatOfDetailText(
                                            label = "Tiền điện",
                                            value = ((invoice.newNumberElectric - invoice.oldNumberElectric) * invoice.electricService.chargeValue).toMoney(),
                                            color = DarkGreen
                                        )
                                        if (invoice.oldElectricImageUrl.isNotEmpty()) {
                                            StatOfDetailImage(
                                                label = "Ảnh số điện cũ",
                                                value = invoice.oldElectricImageUrl,
                                            )
                                        } else {
                                            StatOfDetailText(
                                                label = "Ảnh số điện cũ",
                                                value = invoice.oldElectricImageUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                        if (invoice.newElectricImageUrl.isNotEmpty()) {
                                            StatOfDetailImage(
                                                label = "Ảnh số điện mới",
                                                value = invoice.newElectricImageUrl,
                                            )
                                        } else {
                                            StatOfDetailText(
                                                label = "Ảnh số điện mới",
                                                value = invoice.newElectricImageUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                    } else {
                                        StatOfDetailText(
                                            label = "Giá điện x 1 người",
                                            value = invoice.electricService.chargeValue.toMoney()
                                        )
                                        StatOfDetailText(
                                            label = "Tiền điện x ${invoice.tenant.numberOfOccupants} người",
                                            value = (invoice.tenant.numberOfOccupants * invoice.electricService.chargeValue).toMoney(),
                                            color = DarkGreen
                                        )
                                    }

                                    StatOfDetailTextHeader(label = "Tiền thuê")

                                    if (invoice.rentService.chargeMethod == ChargeMethod.FIXED) {
                                        StatOfDetailText(
                                            label = "Tiền thuê phòng",
                                            value = invoice.rentService.chargeValue.toMoney(),
                                            color = DarkGreen
                                        )
                                    } else {

                                        StatOfDetailText(
                                            label = "Giá thuê phòng x 1 người",
                                            value = invoice.rentService.chargeValue.toMoney()
                                        )

                                        StatOfDetailText(
                                            label = "Tiền thuê phòng x ${invoice.tenant.numberOfOccupants} người",
                                            value = (invoice.tenant.numberOfOccupants * invoice.rentService.chargeValue).toMoney(),
                                            color = DarkGreen
                                        )
                                    }

                                    StatOfDetailTextHeader(label = "Chi phí dịch vụ khác")

                                    invoice.otherServices.forEach { service ->
                                        if (service.chargeMethod == ChargeMethod.FIXED) {
                                            StatOfDetailText(
                                                label = service.name,
                                                value = service.chargeValue.toMoney(),
                                                color = DarkGreen
                                            )
                                        } else {
                                            StatOfDetailText(
                                                label = "${service.name} x ${invoice.tenant.numberOfOccupants} người",
                                                value = (invoice.tenant.numberOfOccupants * service.chargeValue).toMoney(),
                                                color = DarkGreen
                                            )
                                        }
                                    }

                                    StatOfDetailTextHeader(
                                        label = "Tổng tiền",
                                        value = invoice.totalAmount.toMoney(),
                                        colorOfValue = Color(0xFFEF5350)
                                    )

                                    StatOfDetailTextHeader(
                                        label = "Trạng thái",
                                        value = if (invoiceStatusChecked == InvoiceStatus.PAID)
                                            "Đã thanh toán"
                                        else
                                            "Chưa thanh toán",
                                        colorOfValue = if (invoiceStatusChecked == InvoiceStatus.PAID) Color.Green else Color.Red
                                    )

                                    Row(
                                        horizontalArrangement = SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(text = "")
                                        CustomSwitch(onCheckedChange = {
                                            invoiceStatusChecked = if (it) {
                                                InvoiceStatus.PAID
                                            } else {
                                                InvoiceStatus.NOT_PAID
                                            }
                                        }, checked = invoiceStatusChecked == InvoiceStatus.PAID)
                                    }

                                    StatOfDetailText(label = "Ghi chú", value = invoice.note)

                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        24.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),

                    ) {
                    CustomElevatedButton(
                        onClick = {}, text = "Chia sẻ",
                        modifier = Modifier.weight(1f)
                    )
                    CustomElevatedButton(
                        onClick = {
                            viewModel.updateStatusInvoice(
                                invoiceId = invoiceId,
                                status = invoiceStatusChecked
                            )
                        }, text = "Cập nhật",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
    LoadingScreen(isLoading = updateInvoiceStatusState is UiState.Loading)
}
