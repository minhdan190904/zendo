package com.gig.zendo.ui.presentation.financial_report

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.ui.theme.LightBlue
import com.gig.zendo.ui.theme.LightYellow
import com.gig.zendo.utils.getCurrentMonth
import timber.log.Timber
import kotlin.math.abs

@Composable
fun BarChartWithMonthlyRevenue(
    values: List<Long>,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 200.dp,
    selectedMonth: Int,
    onClickBar: (Int) -> Unit
) {
    require(values.size == 12) { "Cần đúng 12 giá trị tương ứng 12 tháng." }
    val maxValueAbs = abs(values.maxBy { abs(it) })
    val valuesDouble = if( maxValueAbs == 0L) {
        List(12) { 0.0 }
    } else {
        values.map { it.toDouble() / maxValueAbs * 100.0 }
    }

    Timber.tag("BarChart")
        .d("Max value absolute: $maxValueAbs, Values: $values, Normalized values: $valuesDouble")

    var selectedBarIndex by remember { mutableIntStateOf(selectedMonth) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            valuesDouble.forEachIndexed { index, value ->
                BarWithLabel(
                    value = value,
                    color = if (value >= 0) DarkGreen else Color(0xFFFFA598),
                    maxHeight = maxHeight,
                    isSelected = index == selectedBarIndex,
                    onClick = {
                        selectedBarIndex = index
                        onClickBar(index)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (1..12).forEach { month ->
                Text(
                    text = "T$month",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = if (month == selectedBarIndex + 1) LightBlue else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(DarkGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Lãi",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA598))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Lỗ vốn",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BarWithLabel(
    value: Double,
    color: Color,
    maxHeight: Dp,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var heightPercent = abs(value).coerceIn(0.0, 100.0) / 100.0
    if (heightPercent < 0.03) heightPercent = 0.03
    val barHeight = with(LocalDensity.current) { (maxHeight.value * heightPercent).dp }
    Spacer(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .height(barHeight)
            .weight(1f)
            .background(color, RoundedCornerShape(8.dp))
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    color = LightBlue,
                    shape = RoundedCornerShape(8.dp)
                ) else Modifier
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    )
}
