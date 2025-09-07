@file:Suppress("DEPRECATION")

package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
private fun Modifier.shimmer(shape: Shape = RoundedCornerShape(8.dp)) = this.placeholder(
    visible = true,
    shape = shape,
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    highlight = PlaceholderHighlight.shimmer(
        highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    )
)

@Composable
private fun PillShimmer(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.shimmer(RoundedCornerShape(6.dp)),
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(Modifier.height(38.dp)) {}
    }
}


@Composable
fun PropertyHouseCardShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            // Header image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                    .shimmer()
            )

            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(20.dp)
                            .shimmer(RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .shimmer(CircleShape)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .shimmer(CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(12.dp)
                            .shimmer(RoundedCornerShape(3.dp))
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PillShimmer(modifier = Modifier.weight(1f))
                    PillShimmer(modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(12.dp))

                PillShimmer(modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(12.dp))

                PillShimmer(modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(12.dp))

                PillShimmer(modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmer(RoundedCornerShape(8.dp))
                )
            }
        }
    }

    Spacer(Modifier.height(16.dp))
}