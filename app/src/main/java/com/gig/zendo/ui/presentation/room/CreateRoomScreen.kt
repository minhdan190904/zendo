package com.gig.zendo.ui.presentation.room

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.Room
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    navController: NavController,
    viewModel: RoomViewModel,
    snackbarHostState: SnackbarHostState,
    houseId: String? = null,
) {
    var roomName by remember {  mutableStateOf("")}
    val createRoomState by viewModel.createRoomState.collectAsStateWithLifecycle()
    var selectedRoom = viewModel.selectedRoom

    LaunchedEffect(selectedRoom) {
        selectedRoom?.let {
            roomName = it.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(selectedRoom == null) "Tạo phòng" else "Chỉnh sửa phòng") },
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
                            label = "Tên phòng",
                            value = roomName,
                            onValueChange = { roomName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Phòng 101",
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {viewModel.addAndUpdateRoom(
                                Room(
                                    id = selectedRoom?.id ?: "",
                                    name = roomName,
                                    houseId = houseId ?: "",
                                    createdAt = selectedRoom?.createdAt ?: System.currentTimeMillis(),
                                )
                            )},
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
    LoadingScreen(isLoading = createRoomState is UiState.Loading)

    LaunchedEffect(createRoomState) {
        when (val state = createRoomState) {
            is UiState.Success<*> -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                viewModel.selectedRoom = null
                viewModel.clearCreateRoomState()
                navController.popBackStack()
                snackbarHostState.showSnackbar("✓ Tạo phòng trọ thành công!")
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
