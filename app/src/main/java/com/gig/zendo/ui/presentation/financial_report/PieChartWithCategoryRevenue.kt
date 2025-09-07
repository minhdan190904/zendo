package com.gig.zendo.ui.presentation.financial_report

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.toMoney

@Composable
fun PieChartWithCategoryRevenue(
    data: Map<String, Pair<Long, Color>>,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 20.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sumOf { it.first }.toFloat()
    val floatValues = data.values.map { 360f * it.first.toFloat() / totalSum }
    val colors = data.values.map { it.second }

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 990f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(animateSize.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .offset { IntOffset.Zero }
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                floatValues.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        startAngle = lastValue,
                        sweepAngle = value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailsPieChart(data = data)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsPieChart(
    data: Map<String, Pair<Long, Color>>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
    ) {
        data.forEach { (label, valueColorPair) ->
            val totalValue = data.values.sumOf { it.first }
            val percentage = if (totalValue == 0L) 0f else valueColorPair.first * 100f / totalValue
            DetailsPieChartItem(
                label = label,
                amount = valueColorPair.first,
                color = valueColorPair.second,
                percentage = percentage
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DetailsPieChartItem(
    label: String,
    amount: Long,
    color: Color,
    percentage: Float,
    height: Dp = 10.dp
) {
    Surface(
        modifier = Modifier.padding(vertical = 10.dp),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(height)
                    .clip(CircleShape)
                    .background(color)
            )

            Column {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = label + " (" + String.format("%.1f", percentage) + "%)",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = amount.toMoney(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = DarkGreen
                )
            }
        }
    }
}