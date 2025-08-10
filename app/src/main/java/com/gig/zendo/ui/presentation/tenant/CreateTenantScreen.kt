package com.gig.zendo.ui.presentation.tenant

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.ChargeMethod
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.Service
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomDisplayImageDialog
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomImagePicker
import com.gig.zendo.ui.common.CustomImagePickerDialog
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.CustomRadioGroup
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.ui.presentation.service.labelMapperForChargeMethod
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getToday
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTenantScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModelTenant: TenantViewModel,
    viewModelHouse: HouseViewModel,
    viewModelRoom: RoomViewModel,
    roomId: String,
    houseId: String,
) {
    val context = LocalContext.current

    val selectedTenant = viewModelRoom.selectedTenant

    var nameTenant by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableStateOf("1") }
    var address by remember { mutableStateOf("") }
    var idCard by remember { mutableStateOf("") }
    var idCardFrontImageUrl by remember { mutableStateOf("") }
    var idCardBackImageUrl by remember { mutableStateOf("") }
    var selectedImageUrl by remember { mutableStateOf("") }
    var isImagePickerOpen by remember { mutableStateOf(false) }
    var deposit by remember { mutableStateOf("") }
    var startDateRent by remember { mutableStateOf(getToday()) }
    val note by remember { mutableStateOf("") }
    var positionImage by remember { mutableIntStateOf(0) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val createTenantState by viewModelTenant.createTenantState.collectAsStateWithLifecycle()
    val upImageState by viewModelTenant.upImageState.collectAsStateWithLifecycle()

    val housesState by viewModelHouse.housesState.collectAsStateWithLifecycle()
    val houseState by viewModelHouse.updateHouseServicesState.collectAsStateWithLifecycle()

    var electricityCharge by remember { mutableStateOf("") }
    var waterCharge by remember { mutableStateOf("") }
    var rentCharge by remember { mutableStateOf("") }
    var electricChargeMethod by remember { mutableStateOf(ChargeMethod.BY_CONSUMPTION) }
    var waterChargeMethod by remember { mutableStateOf(ChargeMethod.BY_CONSUMPTION) }
    var rentChargeMethod by remember { mutableStateOf(ChargeMethod.FIXED) }

    LaunchedEffect(selectedTenant) {
        if(selectedTenant != null){
            nameTenant = selectedTenant.name
            phoneNumber = selectedTenant.phone
            address = selectedTenant.address
            idCard = selectedTenant.identityNumber
            idCardFrontImageUrl = selectedTenant.identityCardFrontUrl
            idCardBackImageUrl = selectedTenant.identityCardBackUrl
            numberOfPeople = selectedTenant.numberOfOccupants.toString()
            deposit = selectedTenant.deposit.toString()
            startDateRent = selectedTenant.startDate
            electricityCharge = selectedTenant.electricService.chargeValue.toString()
            waterCharge = selectedTenant.waterService.chargeValue.toString()
            rentCharge = selectedTenant.rentService.chargeValue.toString()
            electricChargeMethod = selectedTenant.electricService.chargeMethod
            waterChargeMethod = selectedTenant.waterService.chargeMethod
            rentChargeMethod = selectedTenant.rentService.chargeMethod
        } else {
            val house = if (housesState is UiState.Success) {
                if(houseState is UiState.Success) {
                    (houseState as UiState.Success<House>).data
                } else {
                    (housesState as UiState.Success<List<House>>).data.firstOrNull{ it.id == houseId }
                }
            } else null

            house?.let {
                electricityCharge = it.electricService.chargeValue.toString()
                waterCharge = it.waterService.chargeValue.toString()
                rentCharge = it.rentService.chargeValue.toString()
                electricChargeMethod = it.electricService.chargeMethod
                waterChargeMethod = it.waterService.chargeMethod
                rentChargeMethod = it.rentService.chargeMethod
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(selectedTenant == null) "Thêm khách" else "Chỉnh sửa khách") },
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
                                text = "Thông tin khách",
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
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomLabeledTextField(
                                    label = "Khách thuê",
                                    value = nameTenant,
                                    onValueChange = { nameTenant = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: Trần Minh Đan",
                                    inputType = InputType.TEXT
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "Số điện thoại",
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 0348580486",
                                    inputType = InputType.NUMBER
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "Số người ở",
                                    value = numberOfPeople,
                                    onValueChange = { numberOfPeople = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 2",
                                    inputType = InputType.NUMBER
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "Quê quán/Địa chỉ",
                                    value = address,
                                    onValueChange = { address = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 16 thôn An Định, xã Tô Hiệu, huyện Thường Tín, tp Hà Nội",
                                    inputType = InputType.TEXT
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "CMND/CCCD",
                                    value = idCard,
                                    onValueChange = { idCard = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "số CMND/CCCD của khách",
                                    inputType = InputType.TEXT
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                ) {

                                    //positionImage = 0
                                    CustomImagePicker(
                                        label = "CMND mặt trước",
                                        imageUri = idCardFrontImageUrl,
                                        onPickImageClick = {
                                            positionImage = 0
                                            showImagePickerDialog = true
                                        },
                                        onImageClick = {
                                            isImagePickerOpen = true
                                            selectedImageUrl = idCardFrontImageUrl
                                        },
                                        modifier = Modifier.weight(1f)
                                    )

                                    //positionImage = 1
                                    CustomImagePicker(
                                        label = "CMND mặt sau",
                                        imageUri = idCardBackImageUrl,
                                        onPickImageClick = {
                                            positionImage = 1
                                            showImagePickerDialog = true
                                        },
                                        onImageClick = {
                                            isImagePickerOpen = true
                                            selectedImageUrl = idCardBackImageUrl
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "Tiền cọc",
                                    value = deposit,
                                    onValueChange = { deposit = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 1,500,000",
                                    inputType = InputType.MONEY
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomDateTimePicker(
                                    date = startDateRent,
                                    onDateChange = { startDateRent = it },
                                    label = "Ngày bắt đầu thuê"
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomLabeledTextField(
                                    label = "⚡ Giá điện " + if(electricChargeMethod == ChargeMethod.BY_CONSUMPTION) "(đ/kWh)" else "(đ/người)",
                                    value = electricityCharge,
                                    onValueChange = { electricityCharge = it },
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
                                    label = "💧 Giá nước " + if(waterChargeMethod == ChargeMethod.BY_CONSUMPTION) "(đ/khối)" else "(đ/người)",
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
                                    label = "🏠 Giá thuê " + if(rentChargeMethod == ChargeMethod.FIXED) "(đ/tháng)" else "(đ/người/tháng)",
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

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomElevatedButton(onClick = {
                                    viewModelTenant.addAndUpdateTenant(
                                        Tenant(
                                            id = selectedTenant?.id ?: "",
                                            name = nameTenant,
                                            phone = phoneNumber,
                                            address = address,
                                            identityNumber = idCard,
                                            identityCardFrontUrl = idCardFrontImageUrl,
                                            identityCardBackUrl = idCardBackImageUrl,
                                            roomId = roomId,
                                            numberOfOccupants = numberOfPeople.toIntOrNull() ?: 1,
                                            note = note,
                                            startDate = startDateRent,
                                            deposit = deposit.toLongOrNull() ?: 0L,
                                            waterService = Service(
                                                chargeValue = waterCharge.toLongOrNull() ?: 0L,
                                                chargeMethod = waterChargeMethod
                                            ),
                                            electricService = Service(
                                                chargeValue = electricityCharge.toLongOrNull() ?: 0L,
                                                chargeMethod = electricChargeMethod
                                            ),
                                            rentService = Service(
                                                chargeValue = rentCharge.toLongOrNull() ?: 0L,
                                                chargeMethod = rentChargeMethod
                                            ),
                                        )
                                    )
                                }, text = if(selectedTenant == null) "Tạo" else "Cập nhật",)
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
            viewModelTenant.uploadImage(context, it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            showImagePickerDialog = false
            val uri = saveBitmapToCache(context, it)
            viewModelTenant.uploadImage(context, uri)
        }
    }

    if (showImagePickerDialog) {
        CustomImagePickerDialog(
            onGalleryClick = { pickImageLauncher.launch("image/*") },
            onCameraClick = { cameraLauncher.launch() },
            onDismiss = { showImagePickerDialog = false }
        )
    }

    LoadingScreen(isLoading = createTenantState is UiState.Loading || upImageState is UiState.Loading)


    LaunchedEffect(createTenantState) {
        when (val state = createTenantState) {
            is UiState.Success<*> -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                val notification = if(selectedTenant == null) {
                    "✓ Tạo khách hàng mới thành công!"
                } else {
                    "✓ Cập nhật khách hàng thành công!"
                }
                viewModelTenant.clearCreateTenantState()
                viewModelRoom.selectedTenant = null
                navController.popBackStack()
                snackbarHostState.showSnackbar("✓ Tạo khách hàng mới thành công!")
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

    LaunchedEffect(upImageState) {
        when (val state = upImageState) {
            is UiState.Success -> {
                when (positionImage) {
                    0 -> idCardFrontImageUrl = state.data
                    1 -> idCardBackImageUrl = state.data
                }
                viewModelTenant.clearUpImageState()
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

}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.flush()
    fos.close()
    return Uri.fromFile(file)
}