package com.gig.zendo.ui.presentation.tenant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: RoomViewModel,
    roomId: String,
) {

    val roomsState by viewModel.roomsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết") },
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
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {

                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Chỉnh sửa",
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (roomsState is UiState.Success) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val rooms = (roomsState as UiState.Success).data
                                    val tenants =
                                        rooms.firstOrNull { it.first.id == roomId }?.second
                                    val tenant = tenants?.firstOrNull { it.active == true }
                                    if (tenant != null) {

                                        StatOfTenantTextHeader(label = "Thông tin khách thuê")

                                        StatOfTenantText(
                                            label = "Họ tên",
                                            value = tenant.name
                                        )
                                        StatOfTenantText(
                                            label = "Số điện thoại",
                                            value = tenant.phone
                                        )
                                        StatOfTenantText(
                                            label = "Số người ở",
                                            value = tenant.numberOfOccupants.toString()
                                        )
                                        StatOfTenantText(
                                            label = "Quê quán/Địa chỉ",
                                            value = tenant.address
                                        )
                                        StatOfTenantText(
                                            label = "CMND/CCCD",
                                            value = tenant.identityNumber
                                        )
                                        StatOfTenantText(
                                            label = "Ngày thuê",
                                            value = tenant.startDate
                                        )

                                        if (tenant.identityCardFrontUrl.isNotEmpty()) {
                                            StatOfTenantImage(
                                                label = "CMND/CCCD mặt trước",
                                                value = tenant.identityCardFrontUrl,
                                            )
                                        } else {
                                            StatOfTenantText(
                                                label = "CMND/CCCD mặt trước",
                                                value = tenant.identityCardFrontUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                        if (tenant.identityCardBackUrl.isNotEmpty()) {
                                            StatOfTenantImage(
                                                label = "CMND/CCCD mặt sau",
                                                value = tenant.identityCardBackUrl,
                                            )
                                        } else {
                                            StatOfTenantText(
                                                label = "CMND/CCCD mặt sau",
                                                value = tenant.identityCardBackUrl,
                                                valueIsImageUrl = true
                                            )
                                        }

                                        StatOfTenantTextHeader(label = "Thông tin giá thuê")

                                        StatOfTenantText(
                                            label = "Tiền cọc",
                                            value = "${tenant.deposit} đ"
                                        )

                                        StatOfTenantText(
                                            label = "Giá điện (đ/kWh)",
                                            value = "${tenant.electricPrice} đ"
                                        )

                                        StatOfTenantText(
                                            label = "Giá nước (đ/m3)",
                                            value = "${tenant.waterPrice} đ"
                                        )

                                        StatOfTenantText(
                                            label = "Giá thuê (đ/tháng)",
                                            value = "${tenant.rentPrice} đ"
                                        )

                                        StatOfTenantTextHeader(
                                            label = "Trạng thái",
                                            value = if (tenant.active) "Đang thuê" else "Đã trả phòng",
                                            colorOfValue = if (tenant.active) Color.Green else Color.Red
                                        )

                                        StatOfTenantTextHeader(
                                            label = "Ghi chú",
                                            value = tenant.note
                                        )

                                    } else {
                                        Text(
                                            text = "Không có thông tin khách thuê.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatOfTenantTextHeader(
    label: String,
    value: String = "",
    colorOfValue: Color = Color.Black,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 16.sp
            ),
            color = Color.Black
        )

        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = colorOfValue,
        )
    }
}

@Composable
fun StatOfTenantText(
    label: String,
    value: String,
    valueIsImageUrl: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = if (!valueIsImageUrl) value else "Chưa có ảnh",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = if (valueIsImageUrl) Color.Red else Color.Black,
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color.LightGray
    )
}

@Composable
fun StatOfTenantImage(
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = rememberAsyncImagePainter(value),
            contentDescription = "Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}