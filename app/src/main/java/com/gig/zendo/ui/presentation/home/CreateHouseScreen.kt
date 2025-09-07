package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.ui.common.CustomLabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.common.ZendoScaffold
import com.gig.zendo.ui.common.SubmitButton
import com.gig.zendo.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    navController: NavController,
    viewModel: HouseViewModel,
    snackbarHostState: SnackbarHostState,
    uid: String
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val createHouseState by viewModel.createHouseState.collectAsStateWithLifecycle()
    val form by viewModel.houseFormState.collectAsStateWithLifecycle()
    val selectedHouse = viewModel.selectedHouse

    // Prefill khi vào màn
    LaunchedEffect(selectedHouse) {
        if (selectedHouse == null) viewModel.initForCreate()
        else viewModel.initForEdit(selectedHouse)
    }

    val isLoading = (createHouseState is UiState.Loading)
    val canSubmit = form.canSubmit && !isLoading

    ZendoScaffold(
        title = if (selectedHouse == null) "Tạo nhà trọ" else "Chỉnh sửa nhà trọ",
        onBack = {
            viewModel.selectedHouse = null
            navController.popBackStack()
        }
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
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorScheme.primary)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Thông tin nhà",
                            style = typography.titleSmall.copy(fontSize = 16.sp),
                            color = colorScheme.onPrimaryContainer
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        CustomLabeledTextField(
                            label = "Tên nhà",
                            value = form.name.text,
                            onValueChange = viewModel::onHouseNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: Nhà trọ Minh Đan",
                            maxLength = 50 // 👈 áp max length
                        )
                        if (form.name.error != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                form.name.error ?: "",
                                style = typography.labelSmall,
                                color = colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomLabeledTextField(
                            label = "Địa chỉ",
                            value = form.address.text,
                            onValueChange = viewModel::onHouseAddressChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            useInternalLabel = false,
                            placeholder = "Ví dụ: 198 Hai Bà Trưng, Hà Nội",
                            maxLength = 120 // 👈 áp max length
                        )
                        if (form.address.error != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                form.address.error ?: "",
                                style = typography.labelSmall,
                                color = colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        SubmitButton(
                            text = if (selectedHouse == null) "Tạo" else "Cập nhật",
                            enabled = canSubmit,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.submitHouse(uid) }
                        )
                    }
                }
            }
        }
    }

    LoadingScreen(isLoading = isLoading)

    LaunchedEffect(createHouseState) {
        when (val state = createHouseState) {
            is UiState.Success<*> -> {
                viewModel.clearCreateHouseState()
                val notification = if (selectedHouse == null)
                    "✓ Tạo nhà trọ thành công!"
                else
                    "✓ Cập nhật nhà trọ thành công!"

                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshHouses", true)

                viewModel.selectedHouse = null
                navController.popBackStack()
                snackbarHostState.showSnackbar(notification)
            }
            is UiState.Failure -> snackbarHostState.showSnackbar("✗ ${state.error}")
            is UiState.Loading, UiState.Empty -> Unit
        }
    }
}
