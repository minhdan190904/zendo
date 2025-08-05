package com.gig.zendo.ui.presentation.financial_report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.domain.model.FinancialReport
import com.gig.zendo.ui.common.CustomViewDisplayTotalAmount
import com.gig.zendo.ui.presentation.tenant.StatOfDetailText
import com.gig.zendo.ui.presentation.tenant.StatOfDetailTextHeader
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.toMoney

@Composable
fun FinancialReportItem(
    financialReport: FinancialReport
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFA598))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Dữ liệu tháng ${financialReport.month}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                StatOfDetailTextHeader("Tổng quan về phòng")
                StatOfDetailText(
                    label = "Tổng số phòng",
                    value = "${financialReport.totalRooms} phòng",
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Số phòng trống",
                    value = "${financialReport.vacantRooms} phòng",
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Số phòng được thuê",
                    value = "${financialReport.rentedRooms} phòng",
                    haveDivider = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                StatOfDetailTextHeader("Tổng quan về doanh thu")
                StatOfDetailText(
                    label = "Tiền phòng",
                    value = financialReport.roomRevenue.toMoney(),
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Tiền điện",
                    value = financialReport.electricityRevenue.toMoney(),
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Tiền nước",
                    value = financialReport.waterRevenue.toMoney(),
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Các dịch vụ khác",
                    value = financialReport.otherRevenue.toMoney()
                )
                StatOfDetailText(
                    label = "Tổng doanh thu",
                    value = financialReport.totalRevenue.toMoney(),
                    haveDivider = false,
                    colorValue = DarkGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                StatOfDetailTextHeader("Tổng quan về chi phí")

                StatOfDetailText(
                    label = "Tiền điện",
                    value = financialReport.electricityCost.toMoney(),
                    haveDivider = false
                )
                StatOfDetailText(
                    label = "Tiền nước",
                    value = financialReport.waterCost.toMoney(),
                    haveDivider = false
                )

                StatOfDetailText(
                    label = "Các chi phí khác",
                    value = financialReport.otherCosts.toMoney()
                )

                StatOfDetailText(
                    label = "Tổng chi phí",
                    value = financialReport.totalCosts.toMoney(),
                    haveDivider = false,
                    colorValue = Color(0xFFFF7043)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomViewDisplayTotalAmount(
                    label = "Lợi nhuận",
                    amount = financialReport.profit,
                )
            }
        }
    }
}