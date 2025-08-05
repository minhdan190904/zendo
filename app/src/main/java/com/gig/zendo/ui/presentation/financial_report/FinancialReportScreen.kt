package com.gig.zendo.ui.presentation.financial_report

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.theme.LightBlue
import com.gig.zendo.ui.theme.LightGreen
import com.gig.zendo.ui.theme.LightRed
import com.gig.zendo.ui.theme.LightYellow
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getCurrentMonth
import com.gig.zendo.utils.getCurrentYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    viewModelHouse: HouseViewModel
) {

    val financialReportsState by viewModelHouse.financialReportAllMonth.collectAsStateWithLifecycle()
    var selectedMonth by remember { mutableIntStateOf(getCurrentMonth().toInt() - 1) }
    var selectedYear by remember { mutableIntStateOf(getCurrentYear().toInt()) }
    var showPieChartRevenue by remember { mutableStateOf(true) }
    var showPieChartCost by remember { mutableStateOf(true) }

    LaunchedEffect(selectedYear) {
        viewModelHouse.fetchFinancialReportAllMonth(houseId, selectedYear)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Báo cáo tài chính") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    //container light gray
                    containerColor = Color(0xFFF5F5F5),
                    titleContentColor = Color.Black,
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = if (financialReportsState is UiState.Loading) Alignment.Center else Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when (financialReportsState) {
                    is UiState.Loading -> {
                        CustomLoadingProgress()
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
                        val monthlyProfit = values.map { it.profit }
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
                                        text = "Biểu đồ cột lợi nhuận năm 2025",
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
                                    BarChartWithMonthlyRevenue(
                                        values = monthlyProfit,
                                        selectedMonth = selectedMonth
                                    ) {
                                        selectedMonth = it
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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
                                        .clickable(
                                            //no shadow when clickable
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = { showPieChartRevenue = !showPieChartRevenue }
                                        )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Biểu đồ tròn doanh thu theo danh mục",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 16.sp
                                            ),
                                            color = Color.Black
                                        )

                                        Icon(
                                            painter = painterResource(id = if (showPieChartRevenue) R.drawable.ic_drop_up else R.drawable.ic_drop_down),
                                            contentDescription = "is show",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                if (showPieChartRevenue) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .padding(horizontal = 16.dp, vertical = 24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        PieChartWithCategoryRevenue(
                                            data = mapOf(
                                                "Tiền phòng" to Pair(
                                                    values[selectedMonth].roomRevenue,
                                                    LightGreen
                                                ),
                                                "Tiền điện" to Pair(
                                                    values[selectedMonth].electricityRevenue,
                                                    LightYellow
                                                ),
                                                "Tiền nước" to Pair(
                                                    values[selectedMonth].waterRevenue,
                                                    LightBlue
                                                ),
                                                "Tiền dịch vụ khác" to Pair(
                                                    values[selectedMonth].otherRevenue,
                                                    LightRed
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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
                                        .clickable(
                                            //no shadow when clickable
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = { showPieChartCost = !showPieChartCost }
                                        )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Biểu đồ tròn chi phí theo danh mục",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 16.sp
                                            ),
                                            color = Color.Black
                                        )

                                        Icon(
                                            painter = painterResource(id = if (showPieChartCost) R.drawable.ic_drop_up else R.drawable.ic_drop_down),
                                            contentDescription = "is show",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                if (showPieChartCost) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .padding(horizontal = 16.dp, vertical = 24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        PieChartWithCategoryRevenue(
                                            data = mapOf(
                                                "Tiền điện" to Pair(
                                                    values[selectedMonth].electricityCost,
                                                    LightYellow
                                                ),
                                                "Tiền nước" to Pair(
                                                    values[selectedMonth].waterCost,
                                                    LightBlue
                                                ),
                                                "Tiền dịch vụ khác" to Pair(
                                                    values[selectedMonth].otherCosts,
                                                    LightRed
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        FinancialReportItem(values[selectedMonth])

                    }
                }
            }
        }
    }
}