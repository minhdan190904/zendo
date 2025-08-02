package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.Expense
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.getToday
import com.gig.zendo.utils.toMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExpenseRecordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    viewModelHouse: HouseViewModel
) {

    var date by remember { mutableStateOf(getToday()) }
    var electricExpense by remember { mutableStateOf("") }
    var waterExpense by remember { mutableStateOf("") }
    var otherExpenses by remember { mutableStateOf(listOf<Expense>()) }
    var total by remember { mutableLongStateOf(0L) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm chi phí") },
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
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFA598))
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Chi phí nhà",
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

                        CustomDateTimePicker(date = date, onDateChange = {
                            date = it
                        })

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomLabeledTextField(
                            label = "Tiền điện",
                            value = electricExpense,
                            onValueChange = { electricExpense = it },
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: 500,000",
                            inputType = InputType.MONEY,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomLabeledTextField(
                            label = "Tiền nước",
                            value = waterExpense,
                            onValueChange = { waterExpense = it },
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: 200,0000",
                            inputType = InputType.MONEY,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        otherExpenses.forEachIndexed { index, expense ->
                            ExpenseItem(
                                expense = expense,
                                onValueChange = { description, amount ->
                                    otherExpenses = otherExpenses.toMutableList().apply {
                                        this[index] =
                                            expense.copy(description = description, amount = amount)
                                    }
                                    total = otherExpenses.sumOf { it.amount }
                                },
                                onDelete = {
                                    otherExpenses =
                                        otherExpenses.toMutableList().apply { removeAt(index) }
                                    total = otherExpenses.sumOf { it.amount }
                                }
                            )
                        }

                        Button(
                            onClick = {
                                otherExpenses = otherExpenses + Expense("", 0)
                                total = otherExpenses.sumOf { it.amount }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        ) {
                            Text(text = "+ Thêm chi phí", color = DarkGreen)
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Tổng cộng:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = total.toMoney(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentWidth(Alignment.End),
                                    color = DarkGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomElevatedButton(onClick = { /*TODO*/ }, text = "Lưu lại")
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onValueChange: (String, Long) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {

        CustomLabeledTextField(
            label = "Chi phí mới",
            value = expense.description,
            onValueChange = { onValueChange(it, expense.amount) },
            singleLine = true,
            useInternalLabel = false,
            modifier = Modifier.weight(1f),
            placeholder = "Ví dụ: Tiền rác",
            inputType = InputType.TEXT,
        )

        Spacer(modifier = Modifier.width(8.dp))

        CustomLabeledTextField(
            label = "Số tiền",
            value = expense.amount.toString(),
            onValueChange = { amount ->
                val amountLong = amount.toLongOrNull() ?: 0
                onValueChange(expense.description, amountLong)
            },
            singleLine = true,
            useInternalLabel = false,
            modifier = Modifier.weight(1f),
            placeholder = "Ví dụ: 100,000",
            inputType = InputType.MONEY,
        )

        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}