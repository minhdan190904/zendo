@file:OptIn(ExperimentalMaterial3Api::class)

package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.House
import com.gig.zendo.ui.common.QuickActionButton
import com.gig.zendo.ui.common.ZendoScaffold
import com.gig.zendo.ui.presentation.financial_report.BarChartWithMonthlyRevenue
import com.gig.zendo.ui.presentation.financial_report.PieChartWithCategoryRevenue
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.theme.LightBlue
import com.gig.zendo.ui.theme.LightGreen
import com.gig.zendo.ui.theme.LightRed
import com.gig.zendo.ui.theme.LightYellow
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getCurrentMonth
import com.gig.zendo.utils.getCurrentYear
import com.gig.zendo.utils.toMoney

@Composable
fun HouseOverviewScreen(
    houseId: String,
    houseName: String,
    viewModelHouse: HouseViewModel,
    navController: NavController
) {
    val financialReportsState by viewModelHouse.financialReportAllMonth.collectAsStateWithLifecycle()

    var selectedMonth by remember { mutableIntStateOf(getCurrentMonth().toInt() - 1) }
    var selectedYear by remember { mutableIntStateOf(getCurrentYear().toInt()) }
    var showPieChartRevenue by remember { mutableStateOf(true) }
    var showPieChartCost by remember { mutableStateOf(true) }

    val housesState by viewModelHouse.housesState.collectAsStateWithLifecycle()
    var hasLoaded by rememberSaveable(houseId, selectedYear) { mutableStateOf(false) }

    LaunchedEffect(selectedYear, houseId, hasLoaded) {
        if (!hasLoaded) {
            viewModelHouse.fetchFinancialReportAllMonth(houseId, selectedYear)
            hasLoaded = true
        }
    }

    ZendoScaffold(
        title = "Tổng quan nhà trọ $houseName",
        onBack = { navController.popBackStack() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    icon = { Icon(painter = painterResource(R.drawable.ic_no_room), null) },
                    text = "Quản lý phòng",
                    onClick = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("shouldRefreshRooms", true)
                        navController.navigate(Screens.RoomScreen.route + "/${houseId}/${houseName}")
                    },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    icon = { Icon(painter = painterResource(R.drawable.ic_expense_money), null) },
                    text = "Quản lý chi phí",
                    onClick = {
                        navController.navigate(Screens.ExpenseRecordScreen.route + "/${houseId}")
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            QuickActionButton(
                icon = { Icon(painter = painterResource(R.drawable.ic_chat_bot), null, modifier = Modifier.size(24.dp)) },
                text = "Trợ lý AI",
                onClick = {
                    navController.navigate(Screens.ChatbotScreen.route + "/${houseId}")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (housesState is UiState.Success) {
                val house = (housesState as UiState.Success<List<House>>)
                    .data
                    .firstOrNull { it.id == houseId }

                house?.let { h ->
                    val cards = listOf(
                        StatItem("Doanh thu tháng này", h.monthlyRevenue.toMoney()),
                        StatItem(
                            "Chi phí tháng này",
                            h.monthlyExpense.toMoney()
                        ),
                        StatItem("Tổng số phòng", h.numberOfRoom.toString()),
                        StatItem("Số phòng trống", h.numberOfEmptyRoom.toString()),
                        StatItem("Số hóa đơn", h.numberOfInvoice.toString()),
                        StatItem("Số hóa đơn chưa trả", h.numberOfNotPaidInvoice.toString()),
                        StatItem(
                            "Tiền lãi",
                            (h.monthlyRevenue - h.monthlyExpense).toMoney()
                        )
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (i in cards.indices step 2) {
                            val hasSecond = i + 1 < cards.size
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (hasSecond) {
                                    StatCard(cards[i], Modifier.weight(1f))
                                    StatCard(cards[i + 1], Modifier.weight(1f))
                                } else {
                                    StatCard(cards[i], Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }


            // ====== BIỂU ĐỒ (logic từ FinancialReportScreen) ======
            when (financialReportsState) {
                is UiState.Loading -> {
                    // skeleton đơn giản
                    LoadingChartCard(title = "Biểu đồ cột lợi nhuận năm $selectedYear")
                    Spacer(Modifier.height(12.dp))
                    LoadingChartCard(title = "Biểu đồ tròn doanh thu theo danh mục")
                    Spacer(Modifier.height(12.dp))
                    LoadingChartCard(title = "Biểu đồ tròn chi phí theo danh mục")
                }

                is UiState.Failure -> {
                    Text(
                        text = "Lỗi tải dữ liệu: ${(financialReportsState as UiState.Failure).error}",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red)
                    )
                }

                is UiState.Empty -> {
                    Text(
                        text = "Không có dữ liệu báo cáo tài chính",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                }

                is UiState.Success -> {
                    val values = (financialReportsState as UiState.Success).data

                    if (values.isNotEmpty() && selectedMonth !in values.indices) {
                        selectedMonth = values.lastIndex
                    }

                    val monthlyProfit = values.map { it.profit }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            SectionHeader("Biểu đồ cột lợi nhuận năm $selectedYear")
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                BarChartWithMonthlyRevenue(
                                    values = monthlyProfit,
                                    selectedMonth = selectedMonth
                                ) { selectedMonth = it.coerceIn(0, monthlyProfit.lastIndex) }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            SectionHeader(
                                title = "Biểu đồ tròn doanh thu tháng ${selectedMonth + 1}",
                                clickable = true,
                                expanded = showPieChartRevenue
                            ) { showPieChartRevenue = !showPieChartRevenue }

                            if (showPieChartRevenue && values.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    val m = values[selectedMonth]
                                    PieChartWithCategoryRevenue(
                                        data = mapOf(
                                            "Tiền phòng" to (m.roomRevenue to LightGreen),
                                            "Tiền điện" to (m.electricityRevenue to LightYellow),
                                            "Tiền nước" to (m.waterRevenue to LightBlue),
                                            "Dịch vụ khác" to (m.otherRevenue to LightRed)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            SectionHeader(
                                title = "Biểu đồ tròn chi phí tháng ${selectedMonth + 1}",
                                clickable = true,
                                expanded = showPieChartCost
                            ) { showPieChartCost = !showPieChartCost }

                            if (showPieChartCost && values.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    val m = values[selectedMonth]
                                    PieChartWithCategoryRevenue(
                                        data = mapOf(
                                            "Tiền điện" to (m.electricityCost to LightYellow),
                                            "Tiền nước" to (m.waterCost to LightBlue),
                                            "Dịch vụ khác" to (m.otherCosts to LightRed)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    clickable: Boolean = false,
    expanded: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val headerColor = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .let {
                if (clickable) {
                    it.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onClick?.invoke() }
                } else it
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            if (clickable) {
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_drop_up else R.drawable.ic_drop_down),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
private fun LoadingChartCard(title: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SectionHeader(title)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private data class StatItem(val title: String, val value: String)

@Composable
private fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 1.dp,
        modifier = modifier.heightIn(min = 84.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                item.title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                item.value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}