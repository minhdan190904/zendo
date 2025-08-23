package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.User
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.common.FunctionIcon
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.chatbot.ChatbotScreen
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import java.util.Calendar

@Composable
fun HouseScreen(
    viewModel: HouseViewModel,
    viewModelAuth: AuthViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {

    val housesState by viewModel.housesState.collectAsStateWithLifecycle()
    val deleteHouseState by viewModel.deleteHouseState.collectAsStateWithLifecycle()
    val authState by viewModelAuth.authState.collectAsStateWithLifecycle()
    var currentUser by remember {
        mutableStateOf<User?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf<String?>(null)
    }
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val shouldRefresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("shouldRefreshHouses") == true || navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>(
        "shouldRefreshHouses"
    ) == true

    LaunchedEffect(Unit) {
        currentUser = (viewModelAuth.fetchCurrentUser() as UiState.Success<*>).data as User?
        if (shouldRefresh) {
            viewModel.fetchHouses(currentUser?.uid ?: "")
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("shouldRefreshHouses", false)
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "shouldRefreshHouses",
                false
            )
        }
    }

    LaunchedEffect(deleteHouseState) {
        when (deleteHouseState) {
            is UiState.Success -> {
                viewModel.fetchHouses(currentUser?.uid ?: "")
                snackbarHostState.showSnackbar("Xóa nhà trọ thành công")
                viewModel.clearDeleteState()
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar("Lỗi xóa nhà trọ: ${(deleteHouseState as UiState.Failure).error} ")
                viewModel.clearDeleteState()
            }

            else -> {}
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Failure -> {
                snackbarHostState.showSnackbar("Lỗi không đăng xuất được: ${(authState as UiState.Failure).error}")
            }

            is UiState.Success -> {
                navController.navigate(Screens.LoginScreen.route) {
                    popUpTo(Screens.LoginScreen.route) { inclusive = true }
                }
                viewModelAuth.clearAuthState()
                snackbarHostState.showSnackbar("Đăng xuất thành công")
            }

            else -> {
                // No action needed for loading or empty state
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Header background and icon
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFC0B3))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_house),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 32.dp),
                contentScale = ContentScale.FillBounds
            )
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Zendo",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF7043)
                        )
                        Text(
                            text = "Quản lý nhà trọ thật dễ dàng",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (housesState is UiState.Success)
                            Arrangement.Top
                        else
                            Arrangement.Center
                    ) {

                        when (housesState) {
                            is UiState.Empty -> {
                                IconButton(onClick = { navController.navigate(Screens.CreateHouseScreen.route + "/${currentUser?.uid}") }) {
                                    FunctionIcon(
                                        iconRes = R.drawable.ic_add,
                                        contentDescription = "Tạo nhà trọ mới"
                                    )
                                }


                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Bạn chưa tạo nhà trọ nào",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )

                                Text(
                                    text = "Bấm icon + để tạo nhà trọ mới",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            is UiState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = DarkGreen
                                )
                            }

                            is UiState.Failure -> {
                                Text(
                                    text = "Lỗi tải dữ liệu: ${(housesState as UiState.Failure).error}",
                                    color = Color.Red
                                )
                            }

                            is UiState.Success -> {

                                IconButton(onClick = { navController.navigate(Screens.CreateHouseScreen.route + "/${currentUser?.uid}") }) {
                                    FunctionIcon(
                                        iconRes = R.drawable.ic_add,
                                        contentDescription = "Tạo nhà trọ mới"
                                    )
                                }

                                val houses = (housesState as UiState.Success<List<House>>).data

                                LazyColumn {

                                    items(houses.size) { index ->
                                        val house = houses[index]
                                        PropertyHouseCard(
                                            house = house,
                                            onDetailClick = {
                                                navController.currentBackStackEntry
                                                    ?.savedStateHandle
                                                    ?.set("shouldRefreshRooms", true)

                                                navController.navigate(Screens.RoomScreen.route + "/${house.id}" + "/${house.name}")
                                            },
                                            onDeleteClick = {
                                                showDeleteDialog = house.id
                                            },
                                            onExportClick = { /* no-op */ },
                                            onEditClick = {
                                                viewModel.selectedHouse = house
                                                navController.navigate(Screens.CreateHouseScreen.route + "/${currentUser?.uid}")
                                            },
                                            onExpenseDetailClick = {
                                                navController.navigate(Screens.ExpenseRecordScreen.route + "/${house.id}")
                                            },
                                            onAddExpenseClick = {
                                                navController.navigate(Screens.CreateExpenseRecordScreen.route + "/${house.id}")
                                            },
                                            onFinancialReportClick = {
                                                navController.navigate(
                                                    Screens.FinancialReportScreen.route + "/${house.id}"
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    showDeleteDialog?.let { houseId ->
        ConfirmDialog(
            title = "Xóa Nhà",
            message = "Bạn có chắc chắn muốn xóa nhà trọ này?",
            onConfirm = {
                showDeleteDialog = null
                viewModel.deleteHouse(houseId)
            },
            onDismiss = { showDeleteDialog = null }
        )
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Đăng xuất",
            message = "Bạn có chắc chắn muốn đăng xuất?",
            onConfirm = {
                showLogoutDialog = false
                viewModelAuth.logout()
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

    currentUser?.let {
        ProfilePopupMenu(
            onUpgradeProClick = { navController.navigate(Screens.ChatbotScreen.route) },
            onSupportClick = { navController.navigate(Screens.SupportScreen.route) },
            onLogoutClick = {
                showLogoutDialog = true
            },
            currentUser = it,
        )
    }
}

fun getBillingDay(day: Int): Int {
    if (day == -1) {
        val calendar = Calendar.getInstance()
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    return day
}