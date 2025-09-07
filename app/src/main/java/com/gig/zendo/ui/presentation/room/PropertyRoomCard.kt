package com.gig.zendo.ui.presentation.room

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gig.zendo.R
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.MyPopupMenu
import com.gig.zendo.ui.common.SubmitButton
import com.gig.zendo.ui.presentation.home.InfoPillIconNoBorder   // pill đã sửa có colorBackground
import com.gig.zendo.utils.RoomMenuAction
import com.gig.zendo.utils.toMoney

@Composable
fun PropertyRoomCard(
    tenant: Tenant? = null,
    room: Room,
    onCreateInvoice: () -> Unit = {},
    onAddTenant: () -> Unit = {},
    onCheckHistory: () -> Unit = {},
    onCheckOut: () -> Unit = {},
    onCheckDetail: () -> Unit = {},
    onCheckAllInvoices: () -> Unit = {},
    onEditRoom: () -> Unit = {},
    onEditTenant: () -> Unit = {},
) {
    val actionsIfOccupied = listOf(
        RoomMenuAction.Edit, RoomMenuAction.Delete, RoomMenuAction.Invoice,
        RoomMenuAction.History, RoomMenuAction.CheckOut, RoomMenuAction.TenantDetail, RoomMenuAction.EditTenant
    )
    val actionsIfVacant = listOf(RoomMenuAction.Edit, RoomMenuAction.Delete, RoomMenuAction.History)

    val hasUnpaidInvoices = room.numberOfNotPaidInvoice > 0
    val hasDebt = room.outstandingAmount > 0L

    Card(
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.image_room),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                StatusBadgeTranslucent(
                    text = if (tenant != null) "Đang thuê" else "Trống",
                    container = if (tenant != null)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
                    else
                        Color.White.copy(alpha = 0.30f),
                    textColor = if (tenant != null)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
                )
            }

            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MyPopupMenu(actions = if (tenant != null) actionsIfOccupied else actionsIfVacant) {
                        when (it) {
                            RoomMenuAction.Edit -> onEditRoom()
                            RoomMenuAction.Delete -> { }
                            RoomMenuAction.Invoice -> onCheckAllInvoices()
                            RoomMenuAction.History -> onCheckHistory()
                            RoomMenuAction.CheckOut -> onCheckOut()
                            RoomMenuAction.TenantDetail -> onCheckDetail()
                            RoomMenuAction.EditTenant -> onEditTenant()
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                if (tenant != null) {

                    Column {
                        InformationRow(imageVector = Icons.Outlined.Person, info = tenant.name)
                        Spacer(Modifier.height(12.dp))
                        InformationRow(imageVector = Icons.Outlined.Phone, info = tenant.phone.ifBlank { "—" })
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        InfoPillIconNoBorder(
                            icon = R.drawable.ic_bar_chart,
                            label = "HĐ chưa thu",
                            value = room.numberOfNotPaidInvoice.toString(),
                            colorBackground = if (hasUnpaidInvoices)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                        InfoPillIconNoBorder(
                            icon = R.drawable.ic_money_off,
                            label = "Còn nợ",
                            value = room.outstandingAmount.toMoney(),
                            colorBackground = if (hasDebt)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    InfoPillIconNoBorder(
                        icon = R.drawable.ic_bar_chart,
                        label = "Ngày thuê",
                        value = tenant.startDate.ifBlank { "—" },
                        colorBackground = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    SubmitButton(
                        onClick = onCreateInvoice,
                        text = "Tạo hóa đơn",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true
                    )
                } else {
                    Spacer(Modifier.height(12.dp))
                    SubmitButton(
                        onClick = onAddTenant,
                        text = "Thêm khách",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadgeTranslucent(
    text: String,
    container: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = container
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun InformationRow(
    imageVector: ImageVector,
    info: String
){
    Row{
        Icon(imageVector, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(6.dp))
        Text(
            info,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}
