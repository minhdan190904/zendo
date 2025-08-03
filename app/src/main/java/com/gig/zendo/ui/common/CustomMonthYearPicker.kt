package com.gig.zendo.ui.common

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gig.zendo.ui.theme.DarkGreen
import java.util.Calendar

@Composable
fun CustomMonthYearPicker(
    initialMonth: Int = 1,
    initialYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    onDismiss: () -> Unit,
    onConfirm: (month: Int, year: Int) -> Unit
) {
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }
    var selectedYear by remember { mutableIntStateOf(initialYear) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedMonth, selectedYear) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        title = { Text("Chọn tháng/năm") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Năm trước")
                    }
                    Text(
                        text = selectedYear.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Năm sau")
                    }
                }

                Spacer(Modifier.height(16.dp))

                val months = (1..12).map { "T$it" }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    items(months.size) { index ->
                        val monthNumber = index + 1
                        OutlinedButton(
                            onClick = { selectedMonth = monthNumber },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedMonth == monthNumber) DarkGreen else Color.Transparent,
                                contentColor = if (selectedMonth == monthNumber) Color.White else Color.Black
                            ),
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                        ) {
                            Text(months[index])
                        }
                    }
                }
            }
        }
    )
}
