package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.gig.zendo.domain.model.House
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.MyPopupMenu
import com.gig.zendo.ui.common.StatOfProperty
import com.gig.zendo.utils.HouseMenuAction
import com.gig.zendo.utils.toMoney
import getBillingDay
import getCurrentMonth

@Composable
fun PropertyHouseCard(
    house: House = House(),
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onExportClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onExpenseDetailClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = house.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFFF7043)),
                    modifier = Modifier.weight(1f)
                )

                MyPopupMenu(actions = listOf(HouseMenuAction.Edit, HouseMenuAction.Delete, HouseMenuAction.ExportExcel, HouseMenuAction.AddExpense, HouseMenuAction.ExpenseDetail)) {
                    when (it) {
                        HouseMenuAction.Edit -> onEditClick()
                        HouseMenuAction.Delete -> onDeleteClick()
                        HouseMenuAction.ExportExcel -> onExportClick()
                        HouseMenuAction.AddExpense -> onAddExpenseClick()
                        HouseMenuAction.ExpenseDetail -> onExpenseDetailClick()
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
                Text(house.address, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {

                    val pairOfWeight = Pair(1.5f, 1f)
                    StatOfProperty(title = "Tổng phòng:", value = house.numberOfRoom.toString(), pairOfWeight = pairOfWeight)
                    StatOfProperty(title = "Số phòng trống:", value = house.numberOfEmptyRoom.toString(), pairOfWeight = pairOfWeight)
                    StatOfProperty(title = "Số phòng thiếu tiền:", value = house.numberOfNotPaidRoom.toString(), pairOfWeight = pairOfWeight)
                    StatOfProperty(title = "Số tiền còn thiếu:", value = house.unpaidAmount.toMoney(), pairOfWeight = pairOfWeight)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text("Ngày thu:", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text("Tháng " + getCurrentMonth(), fontSize = 12.sp, color = Color.Black)
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(getBillingDay(house.billingDay ?: -1).toString(), fontSize = 20.sp, color = Color(0xFFFF7043))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Doanh thu tháng:", fontSize = 14.sp, color = Color.Black)
                    Text(house.monthlyRevenue.toMoney(), style = MaterialTheme.typography.titleSmall, color = Color(0xFFFF7043))
                }

                CustomElevatedButton(onClick = onDetailClick, text = "Chi tiết")
            }
        }
    }
}
