package com.gig.zendo.ui.presentation.service_record

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.Room
import com.gig.zendo.domain.model.ServiceRecord
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomDisplayImageDialog
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomImageDisplay
import com.gig.zendo.ui.common.CustomImagePicker
import com.gig.zendo.ui.common.CustomImagePickerDialog
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.common.ExposedDropdownField
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.ui.presentation.tenant.StatOfDetailText
import com.gig.zendo.ui.presentation.tenant.StatOfDetailTextHeader
import com.gig.zendo.ui.presentation.tenant.saveBitmapToCache
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.getToday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRecordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModelHouse: HouseViewModel,
    viewModelRoom: RoomViewModel,
    houseId: String,
) {
    val roomsState by viewModelRoom.roomsState.collectAsStateWithLifecycle()
    val createServiceRecordState by viewModelHouse.createServiceRecordState.collectAsStateWithLifecycle()
    val serviceRecordsState by viewModelHouse.serviceRecordsState.collectAsStateWithLifecycle()
    var selectedRecordDate by remember { mutableStateOf(getToday()) }
    var selectedRoomId by remember { mutableStateOf("") }
    var selectedTenantId by remember { mutableStateOf("") }
    var selectedRoomName by remember { mutableStateOf("") }
    var selectedTenantName by remember { mutableStateOf("") }

    var numberElectricity by remember {
        mutableStateOf("0")
    }

    var numberWater by remember {
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

    var numberElectricityImageUrl by remember {
        mutableStateOf("")
    }

    var numberWaterImageUrl by remember {
        mutableStateOf("")
    }

    var selectedImageUrl by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(houseId) {
        viewModelHouse.fetchServiceRecords(houseId)
    }

    LaunchedEffect(createServiceRecordState) {
        when (val state = createServiceRecordState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("✓ Ghi dữ liệu thành công!")
                viewModelHouse.clearCreateServiceRecordState()
                viewModelHouse.fetchServiceRecords(houseId)
            }

            is UiState.Failure -> snackbarHostState.showSnackbar(
                ("✗ " + state.error)
            )

            is UiState.Loading, UiState.Empty -> {
                // Do nothing while loading or empty state
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ghi điện nước") },
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
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomDateTimePicker(
                    date = selectedRecordDate,
                    onDateChange = { selectedRecordDate = it },
                    label = "Chọn ngày ghi"
                )

                Spacer(Modifier.height(16.dp))

                if (roomsState is UiState.Success) {
                    val roomsAndTenants = (roomsState as UiState.Success).data
                    val rooms = roomsAndTenants.map { it.first }
                    val roomOptions = getRoomOptions(rooms)

                    fun setStateForRoomSelection(roomId: String) {
                        selectedRoomId = roomId
                        selectedRoomName = rooms.find { room -> room.id == roomId }?.name ?: ""
                        val tenant =
                            roomsAndTenants.find { it.first.id == roomId }?.second?.firstOrNull()
                        selectedTenantId = tenant?.id ?: ""
                        selectedTenantName = tenant?.name ?: ""
                    }

                    LaunchedEffect(rooms) {
                        if (selectedRoomId.isEmpty()) {
                            setStateForRoomSelection(rooms.firstOrNull()?.id ?: "")
                        }
                    }


                    ExposedDropdownField(
                        label = "Chọn phòng",
                        options = roomOptions,
                        selectedOption = selectedRoomId,
                        onOptionSelected = {
                            setStateForRoomSelection(it)
                        },
                        labelMapper = {
                            rooms.find { room -> room.id == it }?.name ?: it
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                StatOfDetailTextHeader(label = "Thông tin phòng và người thuê")

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = selectedRoomName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color(
                                    0xFFFF7043
                                )
                            ),
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Người thuê: $selectedTenantName",
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CustomLabeledTextField(
                                label = "Số điện",
                                value = numberElectricity,
                                onValueChange = { numberElectricity = it },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                useInternalLabel = false,
                                placeholder = "Ví dụ: 400",
                                inputType = InputType.NUMBER,
                            )

                            CustomLabeledTextField(
                                label = "Số nước",
                                value = numberWater,
                                onValueChange = { numberWater = it },
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
                                label = "Hình điện",
                                imageUri = numberElectricityImageUrl,
                                onPickImageClick = {
                                    positionImage = 0
                                    showImagePickerDialog = true
                                },
                                onImageClick = {
                                    isImagePickerOpen = true
                                    selectedImageUrl = numberElectricityImageUrl
                                }
                            )

                            //positionImage = 1
                            CustomImagePicker(
                                label = "Hình nước",
                                imageUri = numberWaterImageUrl,
                                onPickImageClick = {
                                    positionImage = 1
                                    showImagePickerDialog = true
                                },
                                onImageClick = {
                                    isImagePickerOpen = true
                                    selectedImageUrl = numberWaterImageUrl
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                CustomElevatedButton(onClick = {
                    val serviceRecord = ServiceRecord(
                        date = selectedRecordDate,
                        roomId = selectedRoomId,
                        tenantId = selectedTenantId,
                        numberElectric = numberElectricity.toLongOrNull() ?: 0,
                        numberWater = numberWater.toLongOrNull() ?: 0,
                        electricImageUrl = numberElectricityImageUrl,
                        waterImageUrl = numberWaterImageUrl,
                        houseId = houseId,
                        tenantName = selectedTenantName,
                        roomName = selectedRoomName,
                    )
                    viewModelHouse.createServiceRecord(serviceRecord)
                }, text = "Ghi dữ liệu")

                Spacer(modifier = Modifier.height(16.dp))

                StatOfDetailTextHeader(label = "Dữ liệu đã ghi của phòng $selectedRoomName")

                Spacer(modifier = Modifier.height(8.dp))
                when (val state = serviceRecordsState) {
                    is UiState.Empty -> {
                        Text(
                            text = "Chưa có dữ liệu ghi cho phòng này",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }

                    is UiState.Loading -> {
                        CustomLoadingProgress()
                    }

                    is UiState.Failure -> {
                        Text(
                            text = "Lỗi: ${state.error}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                        )
                    }

                    is UiState.Success -> {
                        Column {
                            val serviceRecords = state.data.filter { it.roomId == selectedRoomId }
                            for (record in serviceRecords) {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ){
                                            Text(text = record.roomName,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = Color(
                                                        0xFFFF7043
                                                    )
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )

                                            IconButton(
                                                onClick = {
                                                },
                                                modifier = Modifier.padding(0.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Delete,
                                                    contentDescription = "Xoá",
                                                    tint = Color(0xFFE0E0E0)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    // Handle edit action
                                                },
                                                modifier = Modifier.padding(0.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Edit,
                                                    contentDescription = "Chỉnh sửa",
                                                    tint = Color(0xFFE0E0E0)
                                                )
                                            }

                                        }
                                        StatOfDetailText(label = "Ngày ghi", value = record.date)
                                        StatOfDetailText(
                                            label = "Người thuê",
                                            value = record.tenantName
                                        )
                                        StatOfDetailText(
                                            label = "Số điện",
                                            value = record.numberElectric.toString()
                                        )
                                        StatOfDetailText(
                                            label = "Số nước",
                                            value = record.numberWater.toString()
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            CustomImageDisplay(imageUri = record.electricImageUrl) {
                                                isImagePickerOpen = true
                                                selectedImageUrl = record.electricImageUrl
                                            }

                                            CustomImageDisplay(imageUri = record.waterImageUrl) {
                                                isImagePickerOpen = true
                                                selectedImageUrl = record.waterImageUrl
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
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
            viewModelRoom.uploadImage(context, it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            showImagePickerDialog = false
            val uri = saveBitmapToCache(context, it)
            viewModelRoom.uploadImage(context, uri)
        }
    }

    if (showImagePickerDialog) {
        CustomImagePickerDialog(
            onGalleryClick = { pickImageLauncher.launch("image/*") },
            onCameraClick = { cameraLauncher.launch() },
            onDismiss = { showImagePickerDialog = false }
        )
    }

    val upImageState by viewModelRoom.upImageState.collectAsStateWithLifecycle()

    LoadingScreen(isLoading = upImageState is UiState.Loading || createServiceRecordState is UiState.Loading)

    LaunchedEffect(upImageState) {
        when (val state = upImageState) {
            is UiState.Success -> {
                when (positionImage) {
                    0 -> numberElectricityImageUrl = state.data
                    1 -> numberWaterImageUrl = state.data
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
}

private fun getRoomOptions(rooms: List<Room>): List<String> {
    rooms.sortedBy { it.name }
    return rooms.map { it.id }
}