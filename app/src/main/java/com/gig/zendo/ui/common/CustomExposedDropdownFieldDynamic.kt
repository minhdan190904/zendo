package com.gig.zendo.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> CustomExposedDropdownFieldDynamic(
    modifier: Modifier = Modifier,
    label: String = "",
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    labelMapper: (T) -> String = { it.toString() },
    isError: Boolean = false,
    supportingText: String? = null
) {
    var inputText by remember { mutableStateOf(selectedOption?.let { labelMapper(it) } ?: "") }
    var isDropdownVisible by remember { mutableStateOf(false) }

    val filteredOptions = options.filter {
        inputText.isBlank() || labelMapper(it).contains(inputText, ignoreCase = true)
    }

    Box(modifier) {
        Column {
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    isDropdownVisible = true
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(label) },
                isError = isError,
                supportingText = supportingText?.let { { Text(it) } },
                singleLine = true
            )
        }

        DropdownMenu(
            expanded = isDropdownVisible && filteredOptions.isNotEmpty(),
            onDismissRequest = { isDropdownVisible = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(labelMapper(option)) },
                    onClick = {
                        inputText = labelMapper(option)
                        onOptionSelected(option)
                        isDropdownVisible = false
                    }
                )
            }
        }
    }
}
