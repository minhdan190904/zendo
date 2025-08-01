package com.gig.zendo.ui.common

import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gig.zendo.R
import com.gig.zendo.ui.theme.DarkGreen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CustomDateTimePicker(
    date: String,
    onDateChange: (String) -> Unit,
    label: String = "Chọn ngày"
) {
    val context = LocalContext.current
    val themedContext = ContextThemeWrapper(context, R.style.CustomDatePickerDialogTheme)
    val calendar = remember { Calendar.getInstance() }

    if (date.isNotEmpty()) {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedDate = sdf.parse(date)
            parsedDate?.let {
                calendar.time = it
            }
        } catch (_: Exception) {
        }
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            themedContext,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formatted = "%02d/%02d/%04d".format(
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                onDateChange(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }

    //add no background shadow when click or hold box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                datePickerDialog.show()
            }
    ) {
        LabeledTextField(
            label = label,
            value = date,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Chọn ngày",
                    tint = DarkGreen
                )
            },
            enabled = false
        )
    }
}
