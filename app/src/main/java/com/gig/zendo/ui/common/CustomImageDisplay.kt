package com.gig.zendo.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gig.zendo.R

@Composable
fun CustomImageDisplay(
    imageUri: String,
    onImageClick: () -> Unit
){
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