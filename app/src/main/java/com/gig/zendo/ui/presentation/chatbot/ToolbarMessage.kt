package com.gig.zendo.ui.presentation.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.R
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun ToolbarMessage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onBackClick() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = ""
            )

            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp),
                painter = painterResource(id = R.drawable.ic_robot),
                contentDescription = ""
            )

            Column(
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = "Trợ lý ảo Zendes",
                    fontSize = 20.sp,
                    color = DarkGreen.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(6.dp)
                            .background(color = Color.Green, shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Online",
                        color = Color.Green,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp), thickness = DividerDefaults.Thickness, color = Color.LightGray
        )
    }
}
