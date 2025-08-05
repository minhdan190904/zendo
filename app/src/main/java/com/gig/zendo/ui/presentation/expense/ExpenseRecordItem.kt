package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.R
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.ui.presentation.tenant.StatOfDetailText
import com.gig.zendo.ui.presentation.tenant.StatOfDetailTextHeader
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.toMoney

@Composable
fun ExpenseRecordItem(
    expenseRecord: ExpenseRecord,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = DarkGreen
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = expenseRecord.date,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = R.drawable.ic_money),
                        contentDescription = null,
                        tint = Color(0xFF00A67E)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = expenseRecord.totalAmount.toMoney(),
                        color = DarkGreen,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatOfDetailTextHeader(label = "Chi tiết chi phí")

            StatOfDetailText(
                label = "Điện",
                value = expenseRecord.electricExpense.toMoney()
            )

            StatOfDetailText(
                label = "Nước",
                value = expenseRecord.waterExpense.toMoney()
            )

            for (item in expenseRecord.otherExpenses) {
                StatOfDetailText(
                    label = item.description,
                    value = item.amount.toMoney()
                )
            }

            StatOfDetailTextHeader(
                label = "Mô tả: ",
                value = expenseRecord.description,
                styleValue = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    32.dp,
                    alignment = Alignment.CenterHorizontally
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        onViewClick()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_eye),
                            contentDescription = "Xem",
                            tint = DarkGreen
                        )
                    }
                    Text(
                        text = "Xem",
                        color = DarkGreen,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        onEditClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Chỉnh sửa",
                            tint = DarkGreen
                        )
                    }
                    Text(
                        text = "Chỉnh sửa",
                        color = DarkGreen,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        onDeleteClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa",
                            tint = Color.Red
                        )
                    }
                    Text(
                        text = "Xóa",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
