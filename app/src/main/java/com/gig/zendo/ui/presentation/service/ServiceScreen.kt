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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.CustomRadioGroup
import com.gig.zendo.ui.common.ExposedDropdownField
import com.gig.zendo.ui.common.FunctionIcon
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(
    navController: NavController,
    viewModelService: ServiceViewModel = hiltViewModel(),
    viewModelHouse: HouseViewModel,
    snackbarHostState: SnackbarHostState,
    houseId: String
) {
    val housesState by viewModelHouse.housesState.collectAsStateWithLifecycle()
    val houseStateUpdateHouse by viewModelHouse.updateHouseServicesState.collectAsStateWithLifecycle()
    val updateHouseServicesState by viewModelHouse.updateHouseServicesState.collectAsStateWithLifecycle()

    var electricChargeMethod by remember { mutableStateOf(ChargeMethod.BY_CONSUMPTION) }

    var waterChargeMethod by remember { mutableStateOf(ChargeMethod.BY_CONSUMPTION) }

    var rentChargeMethod by remember { mutableStateOf(ChargeMethod.FIXED) }

    var billingDay by remember { mutableIntStateOf(1) }

    var electricCharge by remember { mutableStateOf("") }

    var waterCharge by remember { mutableStateOf("") }

    var rentCharge by remember { mutableStateOf("") }

    val createServiceState by viewModelService.createServiceState.collectAsStateWithLifecycle()
    val servicesState by viewModelService.servicesState.collectAsStateWithLifecycle()

    var showCreateExtraServiceDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModelService.fetchServices(houseId)
    }

    LaunchedEffect(housesState) {
        if (housesState is UiState.Success) {
            val house = if (houseStateUpdateHouse is UiState.Success) {
                (houseStateUpdateHouse as UiState.Success<House>).data
            } else {
                (housesState as UiState.Success<List<House>>).data.firstOrNull { it.id == houseId }
            }
            house?.let {
                billingDay = house.billingDay ?: 1
                electricCharge = house.electricService.chargeValue.toString()
                waterCharge = house.waterService.chargeValue.toString()
                rentCharge = house.rentService.chargeValue.toString()
                rentChargeMethod = house.rentService.chargeMethod
                electricChargeMethod = house.electricService.chargeMethod
                waterChargeMethod = house.waterService.chargeMethod
                rentChargeMethod = house.rentService.chargeMethod
            } ?: run {
                snackbarHostState.showSnackbar("Không tìm thấy nhà trọ với ID: $houseId")
            }
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
                            when (housesState) {
                                is UiState.Loading -> {
                                    CustomLoadingProgress()
                                }

                                is UiState.Failure -> {
                                    Text(
                                        text = "Lỗi: ${(housesState as UiState.Failure).error}",
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
                                                billingDay = it
                                            },
                                            labelMapper = { labelMapperForBillingDay(it) },
                                            label = "Ngày thu tiền hàng tháng",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        CustomLabeledTextField(
                                            label = "⚡ Giá điện (đ/kWh)",
                                            value = electricCharge,
                                            onValueChange = { electricCharge = it },
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
                                                electricChargeMethod = it
                                            },
                                            labelMapper = { labelMapperForChargeMethod(it) },
                                            label = "Phương thức tính tiền điện",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        CustomLabeledTextField(
                                            label = "💧 Giá nước (đ/khối)",
                                            value = waterCharge,
                                            onValueChange = { waterCharge = it },
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
                                                waterChargeMethod = it
                                            },
                                            labelMapper = { labelMapperForChargeMethod(it) },
                                            label = "Phương thức tính tiền nước",
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        CustomLabeledTextField(
                                            label = "🏠 Giá thuê (đ/tháng)",
                                            value = rentCharge,
                                            onValueChange = { rentCharge = it },
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
                                                rentChargeMethod = it
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
                                text = "Thêm dịch vụ trên hóa đơn",
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
                            when (servicesState) {
                                is UiState.Loading -> {
                                    CustomLoadingProgress()
                                }

                                is UiState.Failure -> {
                                    Text(
                                        text = "Lỗi: ${(servicesState as UiState.Failure).error}",
                                        color = Color.Red
                                    )
                                }

                                else -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        IconButton(onClick = {
                                            showCreateExtraServiceDialog = true
                                        }) {
                                            FunctionIcon(
                                                iconRes = R.drawable.ic_add,
                                                contentDescription = "Thêm dịch vụ"
                                            )
                                        }
                                        if (servicesState is UiState.Empty) {
                                            Text(
                                                text = "Chưa có dịch vụ nào được tạo.",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else {
                                            val services =
                                                (servicesState as UiState.Success<List<Service>>).data
                                            for (service in services) {
                                                ExtraServiceItem(
                                                    service = service,
                                                    onDeleteExtraService = {},
                                                    onEditExtraService = {}
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showCreateExtraServiceDialog) {
        CreateExtraServiceDialog(
            onDismiss = { showCreateExtraServiceDialog = false },
            onConfirm = { service ->
                viewModelService.addService(service.copy(houseId = houseId))
                showCreateExtraServiceDialog = false
            }
        )
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
                viewModelService.fetchServices(houseId)
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