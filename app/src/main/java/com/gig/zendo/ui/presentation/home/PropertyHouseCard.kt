package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gig.zendo.R
import com.gig.zendo.domain.model.House
import com.gig.zendo.ui.common.MyPopupMenu
import com.gig.zendo.ui.common.SubmitButton
import com.gig.zendo.utils.HouseMenuAction
import com.gig.zendo.utils.getCurrentMonth
import com.gig.zendo.utils.toMoney

@Composable
fun PropertyHouseCard(
    modifier: Modifier = Modifier,
    house: House = House(),
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.image_house),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                CalendarBadgeTranslucent(
                    month = getCurrentMonth(),
                    day = getBillingDay(house.billingDay ?: -1).toString(),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }

            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = house.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    MyPopupMenu(
                        actions = listOf(
                            HouseMenuAction.Edit,
                            HouseMenuAction.Delete,
                        )
                    ) {
                        when (it) {
                            HouseMenuAction.Edit -> onEditClick()
                            HouseMenuAction.Delete -> onDeleteClick()
                        }
                    }
                }


                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Place,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = house.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Hàng 1: Tổng phòng | Phòng thiếu (no border + icon dẫn)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoPillIconNoBorder(
                        icon = R.drawable.ic_house,
                        label = "Tổng phòng",
                        value = house.numberOfRoom.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    InfoPillIconNoBorder(
                        icon = R.drawable.ic_no_room,
                        label = "Phòng trống",
                        value = house.numberOfEmptyRoom.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                InfoPillIconNoBorder(
                    icon = R.drawable.ic_money_off,
                    label = "Còn nợ",
                    value = house.unpaidAmount.toMoney(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                InfoPillIconNoBorder(
                    icon = R.drawable.ic_bar_chart,
                    label = "Doanh thu tháng",
                    value = house.monthlyRevenue.toMoney(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                InfoPillIconNoBorder(
                    icon = R.drawable.ic_expense_money,
                    label = "Chi phí tháng",
                    value = house.monthlyExpense.toMoney(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                SubmitButton(
                    onClick = onDetailClick,
                    text = "Xem chi tiết",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )
            }
        }
    }

    Spacer(Modifier.height(16.dp))
}

@Composable
fun InfoPillIconNoBorder(
    icon: Int,
    label: String,
    value: String,
    colorBackground: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    val colorTint = MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = colorBackground.copy(alpha = 0.10f),
        contentColor = colorTint
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorTint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = colorTint
            )
            Spacer(Modifier.weight(1f))
            Text(
                value,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun CalendarBadgeTranslucent(month: String, day: String, modifier: Modifier = Modifier) {
    val monthLabel = month.padStart(2, '0')
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.30f)
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tháng",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                monthLabel,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                day,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}