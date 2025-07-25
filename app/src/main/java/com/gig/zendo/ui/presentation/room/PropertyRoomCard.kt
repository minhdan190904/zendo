package com.gig.zendo.ui.presentation.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.MyPopupMenu
import com.gig.zendo.ui.common.StatOfProperty
import com.gig.zendo.utils.RoomMenuAction

@Composable
fun PropertyRoomCard(
    tenant: Tenant? = null,
    room: Room,
    onCreateInvoice: () -> Unit = {},
    onAddTenant: () -> Unit = {},
    onCheckHistory: () -> Unit = {},
    onCheckOut: () -> Unit = {},
    onCheckDetail: () -> Unit = {}
) {

    val actionIfRoomNotEmpty = listOf(
        RoomMenuAction.Edit,
        RoomMenuAction.Delete,
        RoomMenuAction.Invoice,
        RoomMenuAction.History,
        RoomMenuAction.CheckOut,
        RoomMenuAction.TenantDetail
    )

    val actionIfRoomEmpty = listOf(
        RoomMenuAction.Edit,
        RoomMenuAction.Delete,
        RoomMenuAction.History
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (tenant != null) Color.White else Color(0xFFF0F0F0)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row: title + menu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFFF7043)),
                    modifier = Modifier.weight(1f)
                )

                MyPopupMenu(
                    actions = if (tenant != null) actionIfRoomNotEmpty else actionIfRoomEmpty,
                ) {
                    when (it) {
                        RoomMenuAction.Edit -> {
                            // Handle edit room action
                        }
                        RoomMenuAction.Delete -> {
                            // Handle delete room action
                        }
                        RoomMenuAction.Invoice -> {
                            onCreateInvoice()
                        }
                        RoomMenuAction.History -> {
                            onCheckHistory()
                        }
                        RoomMenuAction.CheckOut -> {
                            onCheckOut()
                        }
                        RoomMenuAction.TenantDetail -> {
                            onCheckDetail()
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (tenant != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Room stats
                        StatOfProperty(
                            title = "Người thuê:",
                            value = tenant.name,
                            widthOfProperty = 0.75f
                        )
                        StatOfProperty(
                            title = "Số điện thoại:",
                            value = tenant.phone,
                            widthOfProperty = 0.75f
                        )
                        //ngay thue
                        StatOfProperty(
                            title = "Ngày thuê:",
                            value = tenant.startDate,
                            widthOfProperty = 0.75f
                        )
                        // so hoa don chua thu
                        StatOfProperty(
                            title = "Số hóa đơn chưa thu:",
                            value = "0",
                            widthOfProperty = 0.75f
                        )
                        // so tien con no
                        StatOfProperty(
                            title = "Số tiền còn nợ:",
                            value = "0",
                            widthOfProperty = 0.75f
                        )
                    }
                }

            } else {
                Text(text = "Trống", fontWeight = MaterialTheme.typography.bodyMedium.fontWeight, fontSize = 20.sp, color = Color(0xFF666666))
            }

            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                CustomElevatedButton(onClick = {
                    if(tenant != null) {
                        onCreateInvoice()
                    } else {
                        onAddTenant()
                    }
                }, text = if(tenant != null) "Tạo hóa đơn" else "Thêm khách")
            }
        }

    }
}
