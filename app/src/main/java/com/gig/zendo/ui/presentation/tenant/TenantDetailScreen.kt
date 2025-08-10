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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.utils.toMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    tenant: Tenant? = null,
    roomName: String = "",
) {

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
                            StatOfDetailTextHeader(label = roomName)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (tenant != null) {

                                    StatOfDetailTextHeader(label = "Thông tin khách thuê")

                                    StatOfDetailText(
                                        label = "Họ tên",
                                        value = tenant.name
                                    )
                                    StatOfDetailText(
                                        label = "Số điện thoại",
                                        value = tenant.phone
                                    )
                                    StatOfDetailText(
                                        label = "Số người ở",
                                        value = tenant.numberOfOccupants.toString()
                                    )
                                    StatOfDetailText(
                                        label = "Quê quán/Địa chỉ",
                                        value = tenant.address
                                    )
                                    StatOfDetailText(
                                        label = "CMND/CCCD",
                                        value = tenant.identityNumber
                                    )
                                    StatOfDetailText(
                                        label = "Ngày thuê",
                                        value = tenant.startDate
                                    )

                                    if (tenant.identityCardFrontUrl.isNotEmpty()) {
                                        StatOfDetailImage(
                                            label = "CMND/CCCD mặt trước",
                                            value = tenant.identityCardFrontUrl,
                                        )
                                    } else {
                                        StatOfDetailText(
                                            label = "CMND/CCCD mặt trước",
                                            value = tenant.identityCardFrontUrl,
                                            valueIsImageUrl = true
                                        )
                                    }

                                    if (tenant.identityCardBackUrl.isNotEmpty()) {
                                        StatOfDetailImage(
                                            label = "CMND/CCCD mặt sau",
                                            value = tenant.identityCardBackUrl,
                                        )
                                    } else {
                                        StatOfDetailText(
                                            label = "CMND/CCCD mặt sau",
                                            value = tenant.identityCardBackUrl,
                                            valueIsImageUrl = true
                                        )
                                    }

                                    StatOfDetailTextHeader(label = "Thông tin giá thuê")

                                    StatOfDetailText(
                                        label = "Tiền cọc",
                                        value = tenant.deposit.toMoney()
                                    )

                                    StatOfDetailText(
                                        label = "Giá điện " +
                                                if (tenant.electricService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                                    "(đ/kWh)"
                                                } else {
                                                    "(đ/người)"
                                                },
                                        value = tenant.electricService.chargeValue.toMoney()
                                    )

                                    StatOfDetailText(
                                        label = "Giá nước " +
                                                if (tenant.waterService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                                    "(đ/khối)"
                                                } else {
                                                    "(đ/người)"
                                                },
                                        value = tenant.waterService.chargeValue.toMoney()
                                    )

                                    StatOfDetailText(
                                        label = "Giá thuê " +
                                                if (tenant.rentService.chargeMethod == ChargeMethod.FIXED) {
                                                    "(đ/tháng)"
                                                } else {
                                                    "(đ/người/tháng)"
                                                },
                                        value = tenant.rentService.chargeValue.toMoney()
                                    )

                                    StatOfDetailTextHeader(
                                        label = "Trạng thái",
                                        value = if (tenant.active) "Đang thuê" else "Đã trả phòng",
                                        colorOfValue = if (tenant.active) Color.Green else Color.Red
                                    )

                                    StatOfDetailTextHeader(
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

@Composable
fun StatOfDetailTextHeader(
    label: String,
    value: String = "",
    colorOfValue: Color = Color.Black,
    styleValue: TextStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp)
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
            style = styleValue,
            color = colorOfValue,
        )
    }
}

@Composable
fun StatOfDetailText(
    label: String,
    value: String,
    valueIsImageUrl: Boolean = false,
    colorValue: Color = Color.Black,
    haveDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = if (!valueIsImageUrl) value else "Chưa có ảnh",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = if (valueIsImageUrl) Color.Red else colorValue,
        )
    }

    if (haveDivider) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            color = Color.LightGray
        )
    }
}

@Composable
fun StatOfDetailImage(
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