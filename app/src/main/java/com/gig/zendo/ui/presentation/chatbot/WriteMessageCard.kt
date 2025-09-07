package com.gig.zendo.ui.presentation.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gig.zendo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteMessageCard(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        color = Color.White,
        shape = RoundedCornerShape(30.dp),
    ) {
        TextField(
            modifier = Modifier.background(color = Color.White),
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            placeholder = {
                Text(
                    text = "Viết yêu cầu của bạn...",
                    fontWeight = FontWeight.Bold
                )
            },
            trailingIcon = {
                val isEnabled = value.isNotBlank()

                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(enabled = isEnabled) {
                            if (isEnabled) onClickSend()
                        },
                    painter = painterResource(id = R.drawable.ic_send_message),
                    contentDescription = "",
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        if (isEnabled) Color(0xFF1E88E5) else Color.LightGray
                    )
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
    }
}
