package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.House
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    navController: NavController,
    viewModel: HouseViewModel,
    snackbarHostState: SnackbarHostState,
    uid: String
) {
    val createHouseState by viewModel.createHouseState.collectAsStateWithLifecycle()
    var houseName by remember { mutableStateOf("") }
    var houseAddress by remember { mutableStateOf("") }
    val selectedHouse = viewModel.selectedHouse

    LaunchedEffect(selectedHouse) {
        selectedHouse?.let {
            houseName = it.name
            houseAddress = it.address
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedHouse == null) "Tạo nhà trọ" else "Chỉnh sửa nhà trọ") },
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
                            text = "Thông tin nhà",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                            color = Color.Black
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        CustomLabeledTextField(
                            label = "Tên nhà",
                            value = houseName,
                            onValueChange = { houseName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Nhà trọ Minh Đan"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomLabeledTextField(
                            label = "Địa chỉ",
                            value = houseAddress,
                            onValueChange = { houseAddress = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: 198 Hai Bà Trưng, Hà Nội"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.addAndUpdateHouse(
                                    House(
                                        id = selectedHouse?.id ?: "",
                                        name = houseName,
                                        address = houseAddress,
                                        uid = uid,
                                        createdAt = selectedHouse?.createdAt ?: System.currentTimeMillis(),
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = if (selectedHouse == null) "Tạo" else "Cập nhật",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = createHouseState is UiState.Loading)

    LaunchedEffect(createHouseState) {
        when (val state = createHouseState) {
            is UiState.Success<*> -> {
                viewModel.clearCreateHouseState()
                val notification = if(selectedHouse == null) {
                    "✓ Tạo nhà trọ thành công!"
                } else {
                    "✓ Cập nhật nhà trọ thành công!"
                }
                viewModel.selectedHouse = null
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshHouses", true)
                navController.popBackStack()
                snackbarHostState.showSnackbar(notification)
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
