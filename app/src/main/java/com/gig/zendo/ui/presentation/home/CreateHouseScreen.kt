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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    navController: NavController,
    viewModel: HouseViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    uid: String
) {
    val houseName by viewModel.houseName
    val houseAddress by viewModel.houseAddress
    val createHouseState by viewModel.createHouseState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo nhà") },
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
                    // Header màu hồng
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
                            onValueChange = { viewModel.updateHouseName(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Nhà trọ Minh Đan"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomLabeledTextField(
                            label = "Địa chỉ",
                            value = houseAddress,
                            onValueChange = { viewModel.updateHouseAddress(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: 198 Hai Bà Trưng, Hà Nội"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {viewModel.addHouse(houseName, houseAddress, uid)},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                "Lưu lại",
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
                navController.popBackStack()
                snackbarHostState.showSnackbar("✓ Tạo nhà trọ thành công!")
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
