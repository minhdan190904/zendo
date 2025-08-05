package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.CustomMonthYearPicker
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getCurrentYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseRecordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    viewModelHouse: HouseViewModel,
) {

    var showDialogMonthYearPickerStart by remember { mutableStateOf(false) }
    var showDialogMonthYearPickerEnd by remember { mutableStateOf(false) }
    var selectedStartMonth by remember { mutableStateOf(1.toString()) }
    var selectedStartYear by remember { mutableStateOf(getCurrentYear()) }
    var selectedEndMonth by remember { mutableStateOf(12.toString()) }
    var selectedEndYear by remember { mutableStateOf(getCurrentYear()) }

    val expenseRecordsState by viewModelHouse.expenseRecordsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModelHouse.fetchExpenseRecords(houseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết chi phí") },
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
                    containerColor = Color.White,
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                                text = "Lọc theo thời gian",
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

                            Row {
                                Box(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = Color.LightGray,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable(
                                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                            indication = null,
                                        ) {
                                            showDialogMonthYearPickerStart = true
                                        }
                                        .weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Chọn tháng bắt đầu",
                                            tint = Color.Black
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = "Từ ${selectedStartMonth}/${selectedStartYear}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Box(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = Color.LightGray,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable(
                                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                            indication = null,
                                        ) {
                                            showDialogMonthYearPickerEnd = true
                                        }
                                        .weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Chọn tháng kết thúc",
                                            tint = Color.Black
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = "Từ ${selectedEndMonth}/${selectedEndYear}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (expenseRecordsState) {
                    is UiState.Loading -> {
                        CustomLoadingProgress()
                    }

                    is UiState.Empty -> {
                        Text(
                            text = "Không có dữ liệu",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    is UiState.Failure -> {
                        Text(
                            text = "Lỗi: ${(expenseRecordsState as UiState.Failure).error}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    is UiState.Success -> {
                        val expenseRecords =
                            (expenseRecordsState as UiState.Success<List<ExpenseRecord>>).data
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            items(expenseRecords.size) { index ->
                                val expenseRecord = expenseRecords[index]
                                ExpenseRecordItem(
                                    expenseRecord = expenseRecord,
                                    onViewClick = {
                                        // Handle view click

                                    },
                                    onEditClick = {
                                        // Handle edit click

                                    },
                                    onDeleteClick = {
                                        // Handle delete click
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

            }
        }
    }

    if (showDialogMonthYearPickerStart) {
        CustomMonthYearPicker(
            onDismiss = { showDialogMonthYearPickerStart = false },
            onConfirm = { month, year ->
                selectedStartMonth = month.toString()
                selectedStartYear = year.toString()
                showDialogMonthYearPickerStart = false
            },
            initialMonth = selectedStartMonth.toInt(),
            initialYear = selectedStartYear.toInt()
        )
    }

    if (showDialogMonthYearPickerEnd) {
        CustomMonthYearPicker(
            onDismiss = { showDialogMonthYearPickerEnd = false },
            onConfirm = { month, year ->
                selectedEndMonth = month.toString()
                selectedEndYear = year.toString()
                showDialogMonthYearPickerEnd = false
            },
            initialMonth = selectedEndMonth.toInt(),
            initialYear = selectedEndYear.toInt()
        )
    }

}