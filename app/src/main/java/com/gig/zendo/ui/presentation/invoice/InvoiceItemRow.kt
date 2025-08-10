package com.gig.zendo.ui.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.ui.presentation.tenant.getAnnotatedString
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.toMoney

@Composable
fun InvoiceItemRow(
    invoice: Invoice,
    onViewClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    checkedMap: Map<String, Boolean>
) {
    Box(
        modifier = Modifier
            .background(
                if (invoice.status == InvoiceStatus.PAID) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            )
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = invoice.date,
                modifier = Modifier.weight(2f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = invoice.roomName,
                modifier = Modifier.weight(2f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = invoice.totalAmount.toMoney(),
                modifier = Modifier.weight(2f),
                style = MaterialTheme.typography.bodySmall
            )

            if (invoice.status == InvoiceStatus.NOT_PAID) {
                Checkbox(
                    checked = checkedMap[invoice.id] ?: false,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = DarkGreen,
                        uncheckedColor = Color.Gray,
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Đã chọn",
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp),
                    tint = Color.Gray
                )
            }

            Text(
                text = getAnnotatedString("Xem", Color(0xFF0288D1)),
                modifier = Modifier
                    .clickable { onViewClick() }
                    .weight(1.5f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}