package com.gig.zendo.ui.presentation.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.ChargeMethod.*
import com.gig.zendo.domain.model.Service
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomRadioGroup
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LabeledTextField

@Composable
fun CreateExtraServiceDialog(
    onDismiss: () -> Unit,
    onConfirm: (Service) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf(FIXED) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm dịch vụ mới") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledTextField(
                    label = "Tên dịch vụ",
                    value = name,
                    singleLine = true,
                    useInternalLabel = false,
                    onValueChange = { name = it },
                    placeholder = "Vd: tiền rác",
                    inputType = InputType.TEXT
                )

                LabeledTextField(
                    label = "Giá tiền",
                    value = price,
                    singleLine = true,
                    useInternalLabel = false,
                    onValueChange = { price = it },
                    placeholder = "Vd: 100,000",
                    inputType = InputType.MONEY,
                )

                CustomRadioGroup(
                    options = listOf(
                        FIXED,
                        BY_PERSON
                    ),
                    selectedOption = selectedMethod,
                    onOptionSelected = { selectedMethod = it },
                    labelMapper = { labelMapperForChargeMethod(it) },
                    label = "Phương thức tính tiền điện",
                )
            }
        },
        confirmButton = {
            CustomElevatedButton(onClick = {
                onConfirm(
                    Service(
                        name = name,
                        chargeValue = if(price.isNotEmpty()) price.toLong() else 0L,
                        chargeMethod = selectedMethod
                    )
                )
                onDismiss()
            }, text = "Cập nhật")
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Quay lại")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
