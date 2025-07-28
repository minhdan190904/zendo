package com.gig.zendo.ui.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.presentation.tenant.getToday
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun InvoiceCollectionScreen(
    invoices: List<InvoiceUiModel>,
    onSearchClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onViewClick: (InvoiceUiModel) -> Unit,
    onCheckedChange: (InvoiceUiModel, Boolean) -> Unit
) {
    var selectedDateStart by remember { mutableStateOf(getToday()) }
    var selectedDateEnd by remember { mutableStateOf(getToday()) }
    var selectedRoom by remember { mutableStateOf("Tất cả") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Danh sách hoá đơn", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        Column {
            CustomDateTimePicker(
                date = selectedDateStart,
                onDateChange = { selectedDateStart = it },
                label = "Chọn ngày bắt đầu"
            )

            Spacer(Modifier.height(16.dp))

            CustomDateTimePicker(
                date = selectedDateEnd, onDateChange = { selectedDateEnd = it },
                label = "Chọn ngày kết thúc"
            )

            Spacer(Modifier.height(16.dp))

            CustomElevatedButton(onClick = { onSearchClick }, text = "Tim kiếm")
        }

        Spacer(Modifier.height(16.dp))

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ngày", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            Text("Phòng", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            Text("Tổng tiền", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            Text("Đóng", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1.5f)) // Cột nút Xem
        }

        // Invoice List
        LazyColumn {
            items(invoices) { invoice ->
                HorizontalDivider()
                InvoiceItemRow(
                    invoice = invoice,
                    onViewClick = { onViewClick(invoice) },
                    onCheckedChange = { checked -> onCheckedChange(invoice, checked) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomElevatedButton(onClick = onConfirmClick, text = "Xác nhận đóng tiền")
    }
}

@Composable
fun InvoiceItemRow(
    invoice: InvoiceUiModel,
    onViewClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {

    var isChecked by remember { mutableStateOf(invoice.isPaid) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFEBEE))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = invoice.date,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = invoice.room,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = invoice.amount,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedChange(it)
            },
            modifier = Modifier.weight(1f),
            colors = CheckboxDefaults.colors(
                checkedColor = DarkGreen,
                uncheckedColor = Color(0xFFBDBDBD)
            )
        )
        Text(
            text = "Xem",
            color = Color(0xFF0288D1),
            modifier = Modifier
                .clickable { onViewClick() }
                .weight(1.5f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

data class InvoiceUiModel(
    val id: String,
    val date: String,
    val room: String,
    val amount: String,
    val isPaid: Boolean
)

@Preview(showBackground = true)
@Composable
fun InvoiceScreenPreview() {
    val sampleData = listOf(
        InvoiceUiModel("1", "24/07/2025", "3", "345,503 đ", true),
        InvoiceUiModel("2", "25/07/2025", "3", "389,913 đ", false),
        InvoiceUiModel("3", "25/07/2025", "q3", "0 đ", false)
    )
    InvoiceCollectionScreen(
        invoices = sampleData,
        onSearchClick = {},
        onConfirmClick = {},
        onViewClick = {},
        onCheckedChange = { _, _ -> }
    )
}
