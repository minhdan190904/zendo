package com.gig.zendo.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class InputType {
    TEXT, NUMBER, ALPHANUMERIC, EMAIL, PASSWORD, MONEY
}

class MoneyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = originalText.reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val commasBefore = (offset - 1) / 3
                return offset + commasBefore
            }

            override fun transformedToOriginal(offset: Int): Int {
                val commasBefore = (offset - 1) / 4
                return offset - commasBefore
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    inputType: InputType = InputType.TEXT,
    enabled: Boolean = true,
    useInternalLabel: Boolean = false,
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
    labelColor: Color = Color.Black,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        if (!useInternalLabel) {
            Text(
                text = label,
                style = labelStyle,
                color = labelColor
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (onValueChange != null && enabled) {
                    when (inputType) {
                        InputType.TEXT -> {
                            onValueChange(newValue)
                        }
                        InputType.NUMBER, InputType.MONEY -> {
                            if (newValue.all { it.isDigit() }) {
                                onValueChange(newValue)
                            }
                        }
                        InputType.ALPHANUMERIC -> {
                            if (newValue.all { it.isLetterOrDigit() || it.isWhitespace() }) {
                                onValueChange(newValue)
                            }
                        }
                        InputType.EMAIL, InputType.PASSWORD -> {
                            onValueChange(newValue)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
            singleLine = singleLine,
            enabled = enabled,
            label = if (useInternalLabel) { { Text(label, style = labelStyle, color = labelColor) } } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = when (inputType) {
                    InputType.TEXT, InputType.ALPHANUMERIC -> KeyboardType.Text
                    InputType.NUMBER, InputType.MONEY -> KeyboardType.Number
                    InputType.EMAIL -> KeyboardType.Email
                    InputType.PASSWORD -> KeyboardType.Password
                }
            ),
            visualTransformation = when (inputType) {
                InputType.PASSWORD -> PasswordVisualTransformation()
                InputType.MONEY -> MoneyVisualTransformation()
                else -> VisualTransformation.None
            },
            isError = errorMessage != null
        )
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}