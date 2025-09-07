package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.House
import com.gig.zendo.domain.model.User
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.UiState
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
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

    var currentUser by remember { mutableStateOf<User?>(null) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val shouldRefresh =
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshHouses") == true ||
                navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshHouses") == true

    LaunchedEffect(Unit) {
        currentUser = (viewModelAuth.fetchCurrentUser() as UiState.Success<*>).data as User?
        if (shouldRefresh) {
            viewModel.fetchHouses(currentUser?.uid ?: "")
            navController.currentBackStackEntry?.savedStateHandle?.set("shouldRefreshHouses", false)
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
                snackbarHostState.showSnackbar("Lỗi xóa nhà trọ: ${(deleteHouseState as UiState.Failure).error}")
                viewModel.clearDeleteState()
            }

            else -> Unit
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Failure -> snackbarHostState.showSnackbar("Lỗi không đăng xuất được: ${(authState as UiState.Failure).error}")
            is UiState.Success -> {
                navController.navigate(Screens.GoogleLoginScreen.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
                viewModelAuth.clearAuthState()
                viewModel.clearHousesState()
                snackbarHostState.showSnackbar("Đăng xuất thành công")
            }

            else -> Unit
        }
    }

    val colors = MaterialTheme.colorScheme

    val brandBrush = remember {
        Brush.linearGradient(
            listOf(
                Color(0xFF0D47A1),
                Color(0xFF1976D2),
                Color(0xFF42A5F5),
                Color(0xFF64B5F6),
            )
        )
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val canInteractWithDrawer by remember {
        derivedStateOf {
            drawerState.isClosed && !drawerState.isAnimationRunning
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerHeader(
                    name = currentUser?.getUsernameByEmail() ?: "",
                    email = currentUser?.email ?: "",
                    avatarUrl = currentUser?.imageUrl
                )
                Spacer(Modifier.height(8.dp))
                DRAWER_ITEMS.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFF1E6FF),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedContainerColor = Color.Transparent
                        )
                    )
                }

                val context = LocalContext.current
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val versionName = packageInfo.versionName

                NavigationDrawerItem(
                    label = { Text("Phiên bản: $versionName") },
                    selected = false,
                    onClick = {
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_version),
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFF1E6FF),
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    label = { Text("Đăng xuất") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = "Đăng xuất",
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = MaterialTheme.colorScheme.error,
                        unselectedIconColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        if (canInteractWithDrawer) {
                                            scope.launch { drawerState.open() }
                                        }
                                    },
                                    enabled = canInteractWithDrawer
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_menu),
                                        contentDescription = "Menu",
                                        tint = colors.primary
                                    )
                                }

                                Image(
                                    painter = painterResource(R.drawable.logo_app),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp)
                                )

                                Text(
                                    text = "Zendo",
                                    style = TextStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        brush = brandBrush
                                    )
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (housesState is UiState.Success || housesState is UiState.Empty)
                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate(Screens.CreateHouseScreen.route + "/${currentUser?.uid}")
                        },
                        icon = {
                            Icon(
                                painterResource(R.drawable.ic_add),
                                contentDescription = null
                            )
                        },
                        text = { Text("Thêm nhà trọ") },
                        containerColor = colors.secondary,
                        contentColor = colors.onSecondary
                    )
            }
        ) { padding ->
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                isRefreshing = housesState is UiState.Loading,
                onRefresh = {
                    viewModel.fetchHouses(currentUser?.uid ?: "")
                }
            ) {
                when (housesState) {
                    is UiState.Loading -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp
                            )
                        ) {
                            items(5) {
                                PropertyHouseCardShimmer()
                            }
                        }
                    }

                    is UiState.Failure -> {
                        Text(
                            text = "Lỗi tải dữ liệu: ${(housesState as UiState.Failure).error}",
                            color = colors.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }

                    is UiState.Empty -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Bạn chưa tạo nhà trọ nào",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Bấm nút “Thêm nhà trọ” ở góc phải để bắt đầu",
                                color = colors.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    is UiState.Success -> {
                        val houses = (housesState as UiState.Success<List<House>>).data
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp
                            )
                        ) {
                            items(houses.size) { index ->
                                val house = houses[index]
                                PropertyHouseCard(
                                    house = house,
                                    onDetailClick = {
                                        navController.navigate(
                                            Screens.HouseOverviewScreen.route + "/${house.id}/${house.name}"
                                        )
                                    },
                                    onDeleteClick = { showDeleteDialog = house.id },
                                    onEditClick = {
                                        viewModel.selectedHouse = house
                                        navController.navigate(Screens.CreateHouseScreen.route + "/${currentUser?.uid}")
                                    },
                                )
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
                viewModelAuth.logout(context)
            },
            onDismiss = { showLogoutDialog = false }
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