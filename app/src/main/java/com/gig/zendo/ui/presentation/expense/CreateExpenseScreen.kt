package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseScreen() {
    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    var total by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Chi phí ngày", fontWeight = FontWeight.Bold)
        Text(text = "24/07/2025")

        expenses.forEachIndexed { index, expense ->
            ExpenseItem(
                expense = expense,
                onValueChange = { description, amount ->
                    expenses = expenses.toMutableList().apply {
                        this[index] = expense.copy(description = description, amount = amount)
                    }
                    total = expenses.sumOf { it.amount }
                },
                onDelete = {
                    expenses = expenses.toMutableList().apply { removeAt(index) }
                    total = expenses.sumOf { it.amount }
                }
            )
        }

        Button(
            onClick = {
                expenses = expenses + Expense("Chi phí mới", 0)
                total = expenses.sumOf { it.amount }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "+ Thêm chi phí")
        }

        Text(text = "Tổng chi phí: ${total}đ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
    }
}

data class Expense(val description: String, val amount: Int)

@Composable
fun ExpenseItem(
    expense: Expense,
    onValueChange: (String, Int) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        TextField(
            value = expense.description,
            onValueChange = { onValueChange(it, expense.amount) },
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = expense.amount.toString(),
            onValueChange = { onValueChange(expense.description, it.toIntOrNull() ?: 0) },
            modifier = Modifier.width(100.dp)
        )
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview
@Composable
fun PreviewExpenseScreen() {
    ExpenseScreen()
}