package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gig.zendo.R
import com.gig.zendo.domain.model.Expense
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.InputType

@Composable
fun ExpenseItem(
    expense: Expense,
    onValueChange: (String, Long) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {

        CustomLabeledTextField(
            label = "Chi phí mới",
            value = expense.description,
            onValueChange = { onValueChange(it, expense.amount) },
            singleLine = true,
            useInternalLabel = false,
            modifier = Modifier.weight(1f),
            placeholder = "Ví dụ: Tiền rác",
            inputType = InputType.TEXT,
        )

        Spacer(modifier = Modifier.width(8.dp))

        CustomLabeledTextField(
            label = "Số tiền",
            value = expense.amount.toString(),
            onValueChange = { amount ->
                val amountLong = amount.toLongOrNull() ?: 0
                onValueChange(expense.description, amountLong)
            },
            singleLine = true,
            useInternalLabel = false,
            modifier = Modifier.weight(1f),
            placeholder = "Ví dụ: 100,000",
            inputType = InputType.MONEY,
        )

        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}