package com.gig.zendo.ui.presentation.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.Expense
import com.gig.zendo.domain.model.ExpenseRecord
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.common.ZendoScaffold
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
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
    var description by remember { mutableStateOf("") }

    val createState by viewModelHouse.createExpenseRecordState.collectAsStateWithLifecycle()

    ZendoScaffold(
        title = "Thêm chi phí mới",
        onBack = {
            navController.popBackStack()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
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
                            .background(MaterialTheme.colorScheme.primary)
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
                                    // ✅ GUARD index
                                    if (index in otherExpenses.indices) {
                                        otherExpenses = otherExpenses.toMutableList().apply {
                                            this[index] = expense.copy(description = description, amount = amount)
                                        }
                                        total = otherExpenses.sumOf { it.amount }
                                    }
                                },
                                onDelete = {
                                    // ✅ GUARD index
                                    if (index in otherExpenses.indices) {
                                        otherExpenses = otherExpenses.toMutableList().apply {
                                            removeAt(index)
                                        }
                                        total = otherExpenses.sumOf { it.amount }
                                    }
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
                                    text = (total + (electricExpense.toLongOrNull()
                                        ?: 0L) + (waterExpense.toLongOrNull() ?: 0L)).toMoney(),
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

                        CustomLabeledTextField(
                            label = "Mô tả",
                            value = description,
                            onValueChange = { description = it },
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Tháng này quá nhiều chi phí",
                            inputType = InputType.TEXT,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomElevatedButton(onClick = {
                            val expenseRecord = ExpenseRecord(
                                houseId = houseId,
                                date = date,
                                electricExpense = electricExpense.toLongOrNull() ?: 0L,
                                waterExpense = waterExpense.toLongOrNull() ?: 0L,
                                otherExpenses = otherExpenses,
                                description = description,
                                totalAmount = (total + (electricExpense.toLongOrNull()
                                    ?: 0L) + (waterExpense.toLongOrNull() ?: 0L)),
                            )
                            viewModelHouse.createExpenseRecord(expenseRecord)
                        }, text = "Lưu lại")
                    }
                }
            }
        }
    }

    LoadingScreen(isLoading = createState is UiState.Loading)

    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                viewModelHouse.clearCreateExpenseRecordState()
                navController.popBackStack()
                snackbarHostState.showSnackbar("Đã lưu chi phí thành công")
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar(
                    (createState as UiState.Failure).error ?: "Lỗi khi lưu chi phí"
                )
                viewModelHouse.clearCreateExpenseRecordState()
            }

            else -> {}
        }
    }
}