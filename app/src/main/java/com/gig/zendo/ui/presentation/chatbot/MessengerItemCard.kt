package com.gig.zendo.ui.presentation.chatbot


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MessengerItemCard(
    modifier: Modifier = Modifier,
    message: String = ""
) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {

        Surface(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp, bottomStart = 25.dp),
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                text = message,
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSecondary)
            )
        }
    }
}

@Preview
@Composable
fun MessengerItemPreview() {
    MessengerItemCard()
}