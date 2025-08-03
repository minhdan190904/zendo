package com.gig.zendo.ui.presentation.expense

import android.widget.Space
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.ui.common.CustomMonthYearPicker
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.theme.DarkGreen
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
                .verticalScroll(rememberScrollState())
        ) {
            Column {

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

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = DarkGreen)
                                Spacer(Modifier.width(4.dp))
                                Text("1/25/2004", style = MaterialTheme.typography.bodyLarge)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painterResource(id = R.drawable.ic_money), contentDescription = null, tint = Color(0xFF00A67E))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "234,2343 đ",
                                    color = DarkGreen,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterHorizontally),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                IconButton(onClick = {}) {
                                    Icon(painter = painterResource(id = R.drawable.ic_eye), contentDescription = "Xem", tint = DarkGreen)
                                }
                                Text(text = "Xem", color = DarkGreen)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Chỉnh sửa", tint = DarkGreen)
                                }
                                Text(text = "Chỉnh sửa", color = DarkGreen)
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                                }
                                Text(text = "Xóa", color = Color.Red)
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