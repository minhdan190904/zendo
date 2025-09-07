package com.gig.zendo.ui.presentation.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.gig.zendo.ui.common.ZendoScaffold
import com.gig.zendo.ui.common.SubmitButton
import com.gig.zendo.utils.MaxLengthTextFields
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    navController: NavController,
    viewModel: RoomViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    houseId: String? = null,
) {
    val form by viewModel.roomFormState.collectAsStateWithLifecycle()
    val createRoomState by viewModel.createRoomState.collectAsStateWithLifecycle()
    val selectedRoom = viewModel.selectedRoom

    LaunchedEffect(selectedRoom) {
        if (selectedRoom == null) {
            viewModel.initForCreate()
        } else {
            viewModel.initForEdit(selectedRoom)
        }
    }

    ZendoScaffold(
        title = if (selectedRoom == null) "Tạo phòng trọ" else "Chỉnh sửa phòng trọ",
        onBack = { navController.popBackStack() }
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
                            text = "Thông tin phòng",
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
                            value = form.name.text,
                            onValueChange = viewModel::onRoomNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Phòng 101",
                            maxLength = MaxLengthTextFields.MAX_LENGTH_ROOM_NAME,
                            errorMessage = form.name.error
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        SubmitButton(
                            text = if (selectedRoom == null) "Lưu lại" else "Cập nhật",
                            onClick = { viewModel.updateRoom(houseId = houseId ?: "") },
                            enabled = form.canSubmit && createRoomState !is UiState.Loading,
                            modifier = Modifier.fillMaxWidth()
                        )
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

                val notification =
                    if (selectedRoom == null) "✓ Tạo phòng trọ thành công!" else "✓ Cập nhật phòng trọ thành công!"

                // reset
                viewModel.clearCreateRoomState()
                navController.popBackStack()
                snackbarHostState.showSnackbar(notification)
            }
            is UiState.Failure -> {
                snackbarHostState.showSnackbar("✗ ${state.error}")
            }
            is UiState.Loading, UiState.Empty -> Unit
        }
    }
}
