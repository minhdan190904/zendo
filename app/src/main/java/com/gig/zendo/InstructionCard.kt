package com.gig.zendo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InstructionCard(
    item: InstructionItem,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Số thứ tự trong vòng tròn
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .background(color = Color(0xFFF0F0F0), shape = CircleShape)
            ) {
                Text(
                    text = "${item.id}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF333333))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF666666))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = null,
                tint = Color(0xFFFF7043),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
