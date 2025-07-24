package com.gig.zendo.ui.presentation.tenant

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gig.zendo.R
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.common.CustomDateTimePicker
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.CustomImagePicker
import com.gig.zendo.ui.common.CustomImagePickerDialog
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.utils.CloudinaryUploader
import com.gig.zendo.utils.UiState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTenantScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: TenantViewModel = hiltViewModel(),
    roomId: String,
) {
    val context = LocalContext.current

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
    var electricityPrice by remember { mutableStateOf("") }
    var waterPrice by remember { mutableStateOf("") }
    var rentPrice by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var positionImage by remember { mutableStateOf(0) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val createTenantState by viewModel.createTenantState.collectAsStateWithLifecycle()
    val upImageState by viewModel.upImageState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm khách") },
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
                                LabeledTextField(
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

                                LabeledTextField(
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

                                LabeledTextField(
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

                                LabeledTextField(
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

                                LabeledTextField(
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
                                    horizontalArrangement = Arrangement.SpaceBetween,
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
                                        }
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
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                LabeledTextField(
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

                                LabeledTextField(
                                    label = "Giá điện (đ/kWh)",
                                    value = electricityPrice,
                                    onValueChange = { electricityPrice = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 3,000",
                                    inputType = InputType.MONEY
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                LabeledTextField(
                                    label = "Giá nước (đ/khối)",
                                    value = waterPrice,
                                    onValueChange = { waterPrice = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 11,000",
                                    inputType = InputType.MONEY
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                LabeledTextField(
                                    label = "Giá thuê (đ/tháng)",
                                    value = rentPrice,
                                    onValueChange = { rentPrice = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: 1,500,000",
                                    inputType = InputType.MONEY
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                LabeledTextField(
                                    label = "Ghi chú",
                                    value = note,
                                    onValueChange = { note = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = false,
                                    useInternalLabel = false,
                                    placeholder = "Ví dụ: Phòng bị ẩm mốc, cần sửa chữa",
                                    inputType = InputType.TEXT,
                                    heightSize = 140
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomElevatedButton(onClick = {
                                    viewModel.addTenant(
                                        Tenant(
                                            name = nameTenant,
                                            phone = phoneNumber,
                                            address = address,
                                            identityNumber = idCard,
                                            identityCardFrontUrl = idCardFrontImageUrl,
                                            identityCardBackUrl = idCardBackImageUrl,
                                            roomId = roomId,
                                            numberOfOccupants = numberOfPeople.toIntOrNull() ?: 1,
                                            note = note,
                                            rentPrice = rentPrice.toLongOrNull() ?: 0L,
                                            electricPrice = electricityPrice.toLongOrNull() ?: 0L,
                                            waterPrice = waterPrice.toLongOrNull() ?: 0L,
                                            startDate = startDateRent,
                                            deposit = deposit.toLongOrNull() ?: 0L
                                        )
                                    )
                                }, text = "Lưu lại")
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (isImagePickerOpen) {
        Dialog(onDismissRequest = {
            isImagePickerOpen = false
            selectedImageUrl = ""
        }) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(
                        if (selectedImageUrl.isNotEmpty()) Color.Transparent else Color(
                            0xFFE0E0E0
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUrl),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.ic_image),
                        contentDescription = "Placeholder",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
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

    LoadingScreen(isLoading = createTenantState is UiState.Loading || upImageState is UiState.Loading)


    LaunchedEffect(createTenantState) {
        when (val state = createTenantState) {
            is UiState.Success<*> -> {
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

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.flush()
    fos.close()
    return Uri.fromFile(file)
}

fun getToday(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
}