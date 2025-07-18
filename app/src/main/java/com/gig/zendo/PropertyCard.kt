package com.gig.zendo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun PropertyCard(
    title: String,
    address: String,
    roomCount: Int,
    availableCount: Int,
    overdueCount: Int,
    overdueAmount: Int,
    revenueThisMonth: Int,
    billingMonth: String,
    billingDay: Int,
    onDetailClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFFF7043)),
                    modifier = Modifier.weight(1f)
                )
                MyPopupMenu(actions = listOf(MenuAction.Edit, MenuAction.Delete, MenuAction.Export)) {
                    when (it) {
                        MenuAction.Edit -> onMenuClick() // Handle edit action
                        MenuAction.Delete -> onMenuClick() // Handle delete action
                        MenuAction.Export -> onMenuClick() // Handle export action
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Address row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFFFF7043),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(address, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(12.dp))

            // Stats + calendar badge
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    // Room stats
                    StatOfHouse(title = "Tổng phòng:", value = roomCount.toString())
                    StatOfHouse(title = "Số phòng trống:", value = availableCount.toString())
                    StatOfHouse(title = "Số phòng thiếu tiền:", value = overdueCount.toString())
                    StatOfHouse(title = "Số tiền còn thiếu:", value = "${overdueAmount}₫")
                }

                // Calendar badge
                Column {
                    Text("Ngày thu:", fontSize = 14.sp);
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
//                        .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text("Tháng $billingMonth", fontSize = 12.sp, color = Color.Black)
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$billingDay", fontSize = 20.sp, color = Color(0xFFFF7043))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

            Spacer(Modifier.height(12.dp))

            // Footer: revenue + button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Doanh thu tháng:", fontSize = 14.sp)
                    Text("${revenueThisMonth}₫", style = MaterialTheme.typography.titleSmall)
                }
                Button(onClick = onDetailClick) {
                    Text("Chi tiết")
                }
            }
        }
    }
}

@Composable
fun StatOfHouse(
    title: String,
    value: String,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.65f)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
