package com.gig.zendo.ui.presentation.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.CustomRadioGroup
import com.gig.zendo.ui.common.ExposedDropdownField
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.home.HouseViewModel

import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(
    navController: NavController,
    viewModelService: ServiceViewModel = hiltViewModel(),
    viewModelHouse: HouseViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    houseId: String
) {
    val houseState by viewModelHouse.houseState.collectAsStateWithLifecycle()
    val updateHouseServicesState by viewModelHouse.updateHouseServicesState.collectAsStateWithLifecycle()
    val billingDay by viewModelHouse.billingDay
    val electricCharge by viewModelHouse.electricCharge
    val waterCharge by viewModelHouse.waterCharge
    val rentCharge by viewModelHouse.rentCharge
    val electricChargeMethod by viewModelHouse.electricChargeMethod
    val waterChargeMethod by viewModelHouse.waterChargeMethod
    val rentChargeMethod by viewModelHouse.rentChargeMethod


    val createServiceState by viewModelService.createServiceState.collectAsStateWithLifecycle()
    val serviceState by viewModelService.serviceState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModelHouse.getHouseById(houseId)
    }

    LaunchedEffect(houseState) {
        if (houseState is UiState.Success) {
            val house = (houseState as UiState.Success<House>).data
            viewModelHouse.updateBillingDay(house.billingDay ?: -1)
            viewModelHouse.updateElectricCharge(house.electricService!!.chargeValue.toString())
            viewModelHouse.updateWaterCharge(house.waterService!!.chargeValue.toString())
            viewModelHouse.updateRentCharge(house.rentService!!.chargeValue.toString())
            viewModelHouse.updateElectricChargeMethod(house.electricService.chargeMethod)
            viewModelHouse.updateWaterChargeMethod(house.waterService.chargeMethod)
            viewModelHouse.updateRentChargeMethod(house.rentService.chargeMethod)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt nhà trọ") },
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
                                text = "Giá trị mặc định cho các phòng",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                color = Color.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when (houseState) {
                                is UiState.Loading -> {
                                    CustomLoadingProgress()
                                }

                                is UiState.Failure -> {
                                    Text(
                                        text = "Lỗi: ${(houseState as UiState.Failure).error}",
                                        color = Color.Red
                                    )
                                }

                                is UiState.Success -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        ExposedDropdownField(
                                            options = getListDayBilling(),
                                            selectedOption = billingDay,
                                            onOptionSelected = {
                                                viewModelHouse.updateBillingDay(it)
                                            },
                                            labelMapper = { labelMapperForBillingDay(it) },
                                            label = "Ngày thu tiền hàng tháng",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        LabeledTextField(
                                            label = "⚡ Giá điện (đ/kWh)",
                                            value = electricCharge,
                                            onValueChange = { viewModelHouse.updateElectricCharge(it) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 3,000",
                                            inputType = InputType.MONEY
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        CustomRadioGroup(
                                            options = listOf(
                                                ChargeMethod.BY_CONSUMPTION,
                                                ChargeMethod.BY_PERSON
                                            ),
                                            selectedOption = electricChargeMethod,
                                            onOptionSelected = {
                                                viewModelHouse.updateElectricChargeMethod(it)
                                            },
                                            labelMapper = { labelMapperForChargeMethod(it) },
                                            label = "Phương thức tính tiền điện",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        LabeledTextField(
                                            label = "💧 Giá nước (đ/khối)",
                                            value = waterCharge,
                                            onValueChange = { viewModelHouse.updateWaterCharge(it) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 20,000",
                                            inputType = InputType.MONEY
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        CustomRadioGroup(
                                            options = listOf(
                                                ChargeMethod.BY_CONSUMPTION,
                                                ChargeMethod.BY_PERSON
                                            ),
                                            selectedOption = waterChargeMethod,
                                            onOptionSelected = {
                                                viewModelHouse.updateWaterChargeMethod(it)
                                            },
                                            labelMapper = { labelMapperForChargeMethod(it) },
                                            label = "Phương thức tính tiền nước",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        LabeledTextField(
                                            label = "🏠 Giá thuê (đ/tháng)",
                                            value = rentCharge,
                                            onValueChange = { viewModelHouse.updateRentCharge(it) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 1,500,000",
                                            inputType = InputType.MONEY
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        CustomRadioGroup(
                                            options = listOf(
                                                ChargeMethod.FIXED,
                                                ChargeMethod.BY_PERSON
                                            ),
                                            selectedOption = rentChargeMethod,
                                            onOptionSelected = {
                                                viewModelHouse.updateRentChargeMethod(it)
                                            },
                                            labelMapper = { labelMapperForChargeMethod(it) },
                                            label = "Phương thức tính tiền thuê nhà",
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        CustomElevatedButton(onClick = {
                                            viewModelHouse.updateHouseServices(
                                                houseId = houseId,
                                                rentService = Service(
                                                    chargeValue = rentCharge.toLongOrNull() ?: 0L,
                                                    chargeMethod = rentChargeMethod
                                                ),
                                                electricService = Service(
                                                    chargeValue = electricCharge.toLongOrNull()
                                                        ?: 0L,
                                                    chargeMethod = electricChargeMethod
                                                ),
                                                waterService = Service(
                                                    chargeValue = waterCharge.toLongOrNull() ?: 0L,
                                                    chargeMethod = waterChargeMethod
                                                ),
                                                billingDay = billingDay
                                            )
                                        }, text = "Cập nhật")
                                    }
                                }

                                UiState.Empty -> {
                                    Text(text = "Không có dữ liệu nhà trọ")
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
                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFA598))
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Giá trị mặc định cho các phòng",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    LoadingScreen(isLoading = createServiceState is UiState.Loading || updateHouseServicesState is UiState.Loading)

    LaunchedEffect(updateHouseServicesState) {
        when (val state = updateHouseServicesState) {
            is UiState.Success<*> -> {
                snackbarHostState.showSnackbar("✓ Cập nhật dịch vụ mặc định thành công!")
            }

            is UiState.Failure -> snackbarHostState.showSnackbar(
                ("✗ " +
                        state.error)
            )

            is UiState.Loading, UiState.Empty -> {
                // Do nothing while loading or empty state
            }
        }
    }

    LaunchedEffect(createServiceState) {
        when (val state = createServiceState) {
            is UiState.Success<*> -> {
                navController.popBackStack()
                snackbarHostState.showSnackbar("✓ Tạo dịch vụ mới thành công!")
            }

            is UiState.Failure -> snackbarHostState.showSnackbar(
                ("✗ " +
                        state.error)
            )

            is UiState.Loading, UiState.Empty -> {
                // Do nothing while loading or empty state
            }
        }
    }
}


fun labelMapperForBillingDay(day: Int): String {
    if (day < 0) return "Ngày cuối tháng"
    return "Ngày $day"
}

fun labelMapperForChargeMethod(chargeMethod: ChargeMethod): String {
    return when (chargeMethod) {
        ChargeMethod.BY_CONSUMPTION -> "Theo tiêu thụ (số x giá)"
        ChargeMethod.FIXED -> "Theo giá cố định (giá)"
        ChargeMethod.BY_PERSON -> "Theo số người (giá x người)"
    }
}

fun getListDayBilling(): List<Int> {
    return (1..27).toList() + listOf(-1)
}