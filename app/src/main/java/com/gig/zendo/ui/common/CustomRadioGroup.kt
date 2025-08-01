package com.gig.zendo.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun <T> CustomRadioGroup(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    labelMapper: (T) -> String = { it.toString() },
    label: String = "",
    useLabel: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier.selectableGroup().fillMaxWidth().padding(horizontal = 16.dp),) {

        if(useLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = Color.Black
            )
        }

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                        role = Role.RadioButton,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = DarkGreen,
                    ),
                )
                Text(
                    text = labelMapper(option),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomRadioGroupPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(options[0]) }

    CustomRadioGroup(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        labelMapper = { it }
    )
}

