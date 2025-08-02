import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.ui.presentation.home.PropertyHouseCard
import com.gig.zendo.R
import com.gig.zendo.domain.model.House
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.common.FunctionIcon
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.presentation.home.ProfilePopupMenu
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import java.time.YearMonth
import java.util.Calendar

@Composable
fun HouseScreen(
    viewModel: HouseViewModel,
    viewModelAuth: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {

    val housesState by viewModel.housesState.collectAsStateWithLifecycle()
    val deleteHouseState by viewModel.deleteHouseState.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val showLogoutDialog by viewModelAuth.showLogoutDialog.collectAsStateWithLifecycle()
    val authState by viewModelAuth.authState.collectAsStateWithLifecycle()
    val currentUser by viewModelAuth.currentUser.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchHouses(currentUser?.uid ?: "")
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
                            .verticalScroll(rememberScrollState())
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

                                for (house in (housesState as UiState.Success<List<House>>).data) {
                                    PropertyHouseCard(
                                        house = house,
                                        onDetailClick = {
                                            navController.currentBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("shouldRefreshRooms", true)

                                            navController.navigate(Screens.RoomScreen.route + "/${house.id}" + "/${house.name}")
                                        },
                                        onDeleteClick = {
                                            viewModel.showDeleteHouseDialog(house.id)
                                        },
                                        onExportClick = { /* no-op */ },
                                        onEditClick = {
                                        },
                                        onExpenseDetailClick = {},
                                        onAddExpenseClick = {
                                            navController.navigate(Screens.CreateExpenseRecordScreen.route + "/${house.id}")
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

    showDeleteDialog?.let { houseId ->
        ConfirmDialog(
            title = "Xóa Nhà",
            message = "Bạn có chắc chắn muốn xóa nhà trọ này?",
            onConfirm = {
                viewModel.deleteHouse(houseId)
            },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Đăng xuất",
            message = "Bạn có chắc chắn muốn đăng xuất?",
            onConfirm = {
                viewModelAuth.logout()
            },
            onDismiss = { viewModelAuth.dismissLogoutDialog() }
        )
    }

    currentUser?.let {
        ProfilePopupMenu(
            onUpgradeProClick = {},
            onSupportClick = {},
            onLogoutClick = { viewModelAuth.showLogoutDialog() },
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

fun getCurrentMonth(): Int {
    return Calendar.getInstance().get(Calendar.MONTH) + 1
}