package com.gig.zendo.ui.presentation.service

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gig.zendo.domain.model.Service
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


//@Composable
//fun ExtraServiceItem(
//    service: Service,
//    onMenuClick: (Service) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = service.name,
//                style = MaterialTheme.typography.bodyLarge
//            )
//            Text(
//                text = when (service.chargeMethod) {
//                    "fixed" -> "Giá cố định"
//                    "per_person" -> "Theo số người"
//                    else -> "Không xác định"
//                },
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray
//            )
//        }
//
//        Text(
//            text = formatCurrency(service.chargeValue),
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(end = 8.dp)
//        )
//
//        Box {
//            IconButton(onClick = { expanded = true }) {
//                Icon(Icons.Default.MoreVert, contentDescription = "More")
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                DropdownMenuItem(
//                    text = { Text("Sửa") },
//                    onClick = {
//                        expanded = false
//                        onMenuClick(service)
//                    }
//                )
//                DropdownMenuItem(
//                    text = { Text("Xóa") },
//                    onClick = {
//                        expanded = false
//                        // Gọi callback xóa
//                    }
//                )
//            }
//        }
//    }
//}

fun formatCurrency(value: Double): String {
    val formatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale.US))
    return formatter.format(value)
}

