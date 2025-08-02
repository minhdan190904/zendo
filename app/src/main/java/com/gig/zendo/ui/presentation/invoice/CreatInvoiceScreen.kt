package com.gig.zendo.ui.presentation.invoice

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.Service
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomDisplayImageDialog
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomImagePicker
import com.gig.zendo.ui.common.CustomImagePickerDialog
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.ui.presentation.service.ServiceViewModel
import com.gig.zendo.ui.presentation.tenant.saveBitmapToCache
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getToday
import com.gig.zendo.utils.toMoney
import com.gig.zendo.utils.toMoneyWithoutUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    houseId: String,
    viewModelRoom: RoomViewModel,
    viewModel: InvoiceViewModel,
    viewModelService: ServiceViewModel = hiltViewModel()
) {

    var dateCreateInvoice by remember {
        mutableStateOf(getToday())
    }

    var numberWaterPrevious by remember {
        mutableStateOf("0")
    }

    var numberWaterCurrent by remember {
        mutableStateOf("0")
    }

    var numberElectricityPrevious by remember {
        mutableStateOf("0")
    }

    var numberElectricityCurrent by remember {
        mutableStateOf("0")
    }

    var positionImage by remember {
        mutableIntStateOf(0)
    }

    var showImagePickerDialog by remember {
        mutableStateOf(false)
    }

    var isImagePickerOpen by remember {
        mutableStateOf(false)
    }

    var numberElectricityPreviousImageUrl by remember {
        mutableStateOf("")
    }

    var numberElectricityCurrentImageUrl by remember {
        mutableStateOf("")
    }

    var numberWaterPreviousImageUrl by remember {
        mutableStateOf("")
    }

    var numberWaterCurrentImageUrl by remember {
        mutableStateOf("")
    }

    var selectedImageUrl by remember {
        mutableStateOf("")
    }

    var note by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val servicesState by viewModelService.servicesState.collectAsStateWithLifecycle()

    val listServiceExtraUsed = mutableListOf<Service>()

    var listServiceExtraUsedState by remember {
        mutableStateOf(listOf<Service>())
    }

    val roomsAndTenant = viewModelRoom.currentRoomAndTenant.value
    val currentRoom = roomsAndTenant?.first ?: run {
        Toast.makeText(context, "Không tìm thấy phòng hiện tại", Toast.LENGTH_SHORT).show()
        return
    }
    val currentTenant = roomsAndTenant.second

    val createInvoiceState by viewModel.createInvoiceState.collectAsStateWithLifecycle()

    var total by remember {
        mutableLongStateOf(0L)
    }

    LaunchedEffect(Unit) {
        viewModelService.fetchServices(houseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
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
                        .padding(vertical = 16.dp)
                ) {
                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFA598))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                        ) {
                            Text(
                                text = "Thông tin hóa đơn",
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                CustomDateTimePicker(
                                    date = dateCreateInvoice,
                                    onDateChange = { dateCreateInvoice = it },
                                    label = "Ngày"
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if (currentTenant.electricService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                    Text(
                                        text = "Thông tin số điện cũ và mới",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                        color = Color.Black,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        CustomLabeledTextField(
                                            label = "Số điện cũ",
                                            value = numberElectricityPrevious,
                                            onValueChange = { numberElectricityPrevious = it },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 400",
                                            inputType = InputType.NUMBER,
                                        )

                                        CustomLabeledTextField(
                                            label = "Số điện mới",
                                            value = numberElectricityCurrent,
                                            onValueChange = { numberElectricityCurrent = it },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 500",
                                            inputType = InputType.NUMBER,
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {

                                        //positionImage = 0
                                        CustomImagePicker(
                                            label = "Hình điện cũ",
                                            imageUri = numberElectricityPreviousImageUrl,
                                            onPickImageClick = {
                                                positionImage = 0
                                                showImagePickerDialog = true
                                            },
                                            onImageClick = {
                                                isImagePickerOpen = true
                                                selectedImageUrl = numberElectricityPreviousImageUrl
                                            }
                                        )

                                        //positionImage = 1
                                        CustomImagePicker(
                                            label = "Hình điện mới",
                                            imageUri = numberElectricityCurrentImageUrl,
                                            onPickImageClick = {
                                                positionImage = 1
                                                showImagePickerDialog = true
                                            },
                                            onImageClick = {
                                                isImagePickerOpen = true
                                                selectedImageUrl = numberElectricityCurrentImageUrl
                                            }
                                        )
                                    }

                                } else {
                                    CustomInformation(
                                        label = "Điện tính theo số người: ${currentTenant.numberOfOccupants} người"
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (currentTenant.waterService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                    Text(
                                        text = "Thông tin số nước cũ và mới",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                        color = Color.Black,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        CustomLabeledTextField(
                                            label = "Số nước cũ",
                                            value = numberWaterPrevious,
                                            onValueChange = { numberWaterPrevious = it },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 400",
                                            inputType = InputType.NUMBER,
                                        )

                                        CustomLabeledTextField(
                                            label = "Số nước mới",
                                            value = numberWaterCurrent,
                                            onValueChange = { numberWaterCurrent = it },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            useInternalLabel = false,
                                            placeholder = "Ví dụ: 500",
                                            inputType = InputType.NUMBER,
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {

                                        //positionImage = 2
                                        CustomImagePicker(
                                            label = "Hình nước cũ",
                                            imageUri = numberWaterPreviousImageUrl,
                                            onPickImageClick = {
                                                positionImage = 2
                                                showImagePickerDialog = true
                                            },
                                            onImageClick = {
                                                isImagePickerOpen = true
                                                selectedImageUrl = numberWaterPreviousImageUrl
                                            }
                                        )

                                        //positionImage = 3
                                        CustomImagePicker(
                                            label = "Hình nước mới",
                                            imageUri = numberWaterCurrentImageUrl,
                                            onPickImageClick = {
                                                positionImage = 3
                                                showImagePickerDialog = true
                                            },
                                            onImageClick = {
                                                isImagePickerOpen = true
                                                selectedImageUrl = numberWaterCurrentImageUrl
                                            }
                                        )
                                    }
                                } else {
                                    CustomInformation(
                                        label = "Nước tính theo số người: ${currentTenant.numberOfOccupants} người"
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Tiền dịch vụ thêm",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                when (servicesState) {
                                    is UiState.Loading -> {
                                        CustomLoadingProgress()
                                    }

                                    is UiState.Failure -> {
                                        Text(text = "Lỗi tải dịch vụ: ${(servicesState as UiState.Failure).error}")
                                    }

                                    is UiState.Empty -> {
                                        Text(text = "Không có dịch vụ nào.")
                                    }

                                    is UiState.Success -> {
                                        val services =
                                            (servicesState as UiState.Success<List<Service>>).data

                                        val checkedMap =
                                            remember { mutableStateMapOf<String, Boolean>() }
                                        val amountMap =
                                            remember { mutableStateMapOf<String, String>() }

                                        Column {
                                            services.forEach { service ->
                                                val isChecked = checkedMap[service.id] ?: false
                                                val amount = amountMap[service.id]
                                                    ?: service.chargeValue.toString()

                                                CustomCheckBoxExtraService(
                                                    label = service.name,
                                                    checked = isChecked,
                                                    onCheckedChange = {
                                                        checkedMap[service.id] = it

                                                        if (it) {
                                                            val charge = amount.toLongOrNull() ?: 0L
                                                            listServiceExtraUsed.add(
                                                                service.copy(
                                                                    chargeValue = charge
                                                                )
                                                            )
                                                        } else {
                                                            listServiceExtraUsed.removeAll { it.id == service.id }
                                                        }

                                                        listServiceExtraUsedState =
                                                            listServiceExtraUsed.toList()
                                                    },
                                                    amountOfMoney = amount,
                                                    onAmountChange = {
                                                        amountMap[service.id] = it
                                                        if (checkedMap[service.id] == true) {
                                                            val charge = it.toLongOrNull() ?: 0L
                                                            listServiceExtraUsed.replaceAll { s ->
                                                                if (s.id == service.id) s.copy(
                                                                    chargeValue = charge
                                                                ) else s
                                                            }

                                                            listServiceExtraUsedState =
                                                                listServiceExtraUsed.toList()
                                                        }
                                                    }
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomLabeledTextField(
                                    label = "Ghi chú",
                                    value = note,
                                    onValueChange = { note = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví du: Tiền điện tháng này tăng",
                                    inputType = InputType.TEXT,
                                )

                                val headerWeight = listOf(1.5f, 0.8f, 1f, 1f)
                                val rows = listOf(
                                    Triple(
                                        "Tiền điện",
                                        if (currentTenant.electricService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                            val current =
                                                numberElectricityCurrent.toLongOrNull() ?: 0L
                                            val previous =
                                                numberElectricityPrevious.toLongOrNull() ?: 0L
                                            if (current >= previous) {
                                                current - previous
                                            } else {
                                                0L
                                            }
                                        } else {
                                            currentTenant.numberOfOccupants.toLong()
                                        },
                                        currentTenant.electricService.chargeValue
                                    ),
                                    Triple(
                                        "Tiền nước",
                                        if (currentTenant.waterService.chargeMethod == ChargeMethod.BY_CONSUMPTION) {
                                            val current = numberWaterCurrent.toLongOrNull() ?: 0L
                                            val previous = numberWaterPrevious.toLongOrNull() ?: 0L
                                            if (current >= previous) {
                                                current - previous
                                            } else {
                                                0L
                                            }
                                        } else {
                                            currentTenant.numberOfOccupants.toLong()
                                        }, currentTenant.waterService.chargeValue
                                    ),
                                    Triple(
                                        "Tiền phòng",
                                        if (currentTenant.rentService.chargeMethod == ChargeMethod.BY_PERSON) {
                                            currentTenant.numberOfOccupants.toLong()
                                        } else {
                                            1L
                                        }, currentTenant.rentService.chargeValue
                                    ),
                                )

                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row {
                                        Text(
                                            " ",
                                            modifier = Modifier.weight(headerWeight[0]),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "Số",
                                            modifier = Modifier.weight(headerWeight[1]),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "Giá",
                                            modifier = Modifier.weight(headerWeight[2]),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "Tiền",
                                            modifier = Modifier.weight(headerWeight[3]),
                                            textAlign = TextAlign.End,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    rows.forEach { (label, count, price) ->
                                        Row {
                                            Text(
                                                label,
                                                modifier = Modifier.weight(headerWeight[0]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "$count",
                                                modifier = Modifier.weight(headerWeight[1]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                price.toMoneyWithoutUnit(),
                                                modifier = Modifier.weight(headerWeight[2]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                (count * price).toMoneyWithoutUnit(),
                                                modifier = Modifier.weight(headerWeight[3]),
                                                textAlign = TextAlign.End,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        HorizontalDivider()
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    listServiceExtraUsedState.forEach { service ->
                                        Row {
                                            Text(
                                                service.name,
                                                modifier = Modifier.weight(headerWeight[0]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "1",
                                                modifier = Modifier.weight(headerWeight[1]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                service.chargeValue.toMoneyWithoutUnit(),
                                                modifier = Modifier.weight(headerWeight[2]),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                service.chargeValue.toMoneyWithoutUnit(),
                                                modifier = Modifier.weight(headerWeight[3]),
                                                textAlign = TextAlign.End,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        HorizontalDivider()
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    total = rows.sumOf { it.second * it.third } +
                                            listServiceExtraUsedState.sumOf { it.chargeValue }

                                    // Total Row
                                    Row {
                                        Text(
                                            "Tổng tiền",
                                            modifier = Modifier.weight(headerWeight[0]),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.weight(headerWeight[1] + headerWeight[2]))
                                        Text(
                                            total.toMoney(),
                                            modifier = Modifier.weight(headerWeight[3]),
                                            textAlign = TextAlign.End,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }

                                }
                                CustomElevatedButton(onClick = {
                                    val invoice = Invoice(
                                        tenant = currentTenant,
                                        date = dateCreateInvoice,
                                        oldNumberElectric = numberElectricityPrevious.toLongOrNull()
                                            ?: 0L,
                                        newNumberElectric = numberElectricityCurrent.toLongOrNull()
                                            ?: 0L,
                                        oldNumberWater = numberWaterPrevious.toLongOrNull() ?: 0L,
                                        newNumberWater = numberWaterCurrent.toLongOrNull() ?: 0L,
                                        rentService = currentTenant.rentService,
                                        electricService = currentTenant.electricService,
                                        waterService = currentTenant.waterService,
                                        oldElectricImageUrl = numberElectricityPreviousImageUrl,
                                        newElectricImageUrl = numberElectricityCurrentImageUrl,
                                        oldWaterImageUrl = numberWaterPreviousImageUrl,
                                        newWaterImageUrl = numberWaterCurrentImageUrl,
                                        otherServices = listServiceExtraUsedState,
                                        note = note,
                                        totalAmount = total,
                                        roomId = currentRoom.id,
                                        roomName = currentRoom.name,
                                        houseId = houseId,
                                    )

                                    viewModel.addInvoice(invoice)

                                }, text = "Tạo hóa đơn")
                            }
                        }
                    }
                }
            }
        }
    }

    if (isImagePickerOpen) {
        CustomDisplayImageDialog(selectedImageUrl = selectedImageUrl) {
            isImagePickerOpen = false
            selectedImageUrl = ""
        }
    }


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            showImagePickerDialog = false
            viewModel.uploadImage(context, it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            showImagePickerDialog = false
            val uri = saveBitmapToCache(context, it)
            viewModel.uploadImage(context, uri)
        }
    }

    if (showImagePickerDialog) {
        CustomImagePickerDialog(
            onGalleryClick = { pickImageLauncher.launch("image/*") },
            onCameraClick = { cameraLauncher.launch() },
            onDismiss = { showImagePickerDialog = false }
        )
    }

    val upImageState by viewModel.upImageState.collectAsStateWithLifecycle()

    LoadingScreen(isLoading = upImageState is UiState.Loading || createInvoiceState is UiState.Loading)

    LaunchedEffect(upImageState) {
        when (val state = upImageState) {
            is UiState.Success -> {
                when (positionImage) {
                    0 -> numberElectricityPreviousImageUrl = state.data
                    1 -> numberElectricityCurrentImageUrl = state.data
                    2 -> numberWaterPreviousImageUrl = state.data
                    3 -> numberWaterCurrentImageUrl = state.data
                }
                snackbarHostState.showSnackbar("✓ Tải ảnh lên thành công!")
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

    LaunchedEffect(createInvoiceState) {
        when (val state = createInvoiceState) {
            is UiState.Success -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                val invoiceCurrent = state.data
                navController.navigate(Screens.InvoiceDetailScreen.route + "/${invoiceCurrent.id}")
                snackbarHostState.showSnackbar("✓ Tạo hóa đơn thành công!")
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

@Composable
fun CustomInformation(
    label: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info, contentDescription = "Thông tin",
                tint = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CustomCheckBoxExtraService(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    amountOfMoney: String,
    onAmountChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked, onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = DarkGreen,
                uncheckedColor = Color.Gray,
            )
        )
        CustomLabeledTextField(
            label = label,
            value = amountOfMoney,
            onValueChange = { onAmountChange(it) },
            singleLine = true,
            useInternalLabel = false,
            placeholder = "Ví dụ: 20,000",
            inputType = InputType.MONEY,
        )
    }
}