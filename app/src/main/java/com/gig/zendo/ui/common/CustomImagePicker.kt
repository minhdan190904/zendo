package com.gig.zendo.ui.common

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gig.zendo.R

@Composable
fun CustomImagePicker(
    label: String,
    imageUri: String,
    onPickImageClick: () -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            )
            IconButton(onClick = onPickImageClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pick image",
                    tint = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .size(width = 140.dp, height = 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0E0E0))
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = rememberAsyncImagePainter(R.drawable.ic_image),
                    contentDescription = "Placeholder",
                    tint = Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}