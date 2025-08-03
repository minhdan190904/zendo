package com.gig.zendo.ui.presentation.navigation

import HouseScreen
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.ui.presentation.auth.login.GoogleLoginScreen
import com.gig.zendo.ui.presentation.auth.login.LoginScreen
import com.gig.zendo.ui.presentation.auth.register.RegisterScreen
import com.gig.zendo.ui.presentation.expense.CreateExpenseRecordScreen
import com.gig.zendo.ui.presentation.expense.ExpenseRecordScreen
import com.gig.zendo.ui.presentation.home.CreateHouseScreen
import com.gig.zendo.ui.presentation.home.HouseViewModel
import com.gig.zendo.ui.presentation.instruction.InstructionScreen
import com.gig.zendo.ui.presentation.invoice.AcceptInvoiceScreen
import com.gig.zendo.ui.presentation.invoice.CreateInvoiceScreen
import com.gig.zendo.ui.presentation.invoice.InvoiceDetailScreen
import com.gig.zendo.ui.presentation.invoice.InvoiceHistoryScreen
import com.gig.zendo.ui.presentation.invoice.InvoiceViewModel
import com.gig.zendo.ui.presentation.room.CreateRoomScreen
import com.gig.zendo.ui.presentation.room.RoomScreen
import com.gig.zendo.ui.presentation.room.RoomViewModel
import com.gig.zendo.ui.presentation.service.ServiceScreen
import com.gig.zendo.ui.presentation.service_record.ServiceRecordScreen
import com.gig.zendo.ui.presentation.tenant.CreateTenantScreen
import com.gig.zendo.ui.presentation.tenant.TenantDetailScreen
import com.gig.zendo.ui.presentation.tenant.TenantHistoryScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val houseViewModel: HouseViewModel = hiltViewModel()
    val roomViewModel: RoomViewModel = hiltViewModel()
    val invoiceViewModel: InvoiceViewModel = hiltViewModel()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = when {
                        "✓" in data.visuals.message -> Color.Green
                        "✗" in data.visuals.message -> Color.Red
                        else -> Color.DarkGray
                    },
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.HouseScreen.route,
            modifier = Modifier.padding(0.dp)
        ) {
            composable(
                route = Screens.LoginScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                LoginScreen(navController = navController, snackbarHostState = snackbarHostState)
            }
            composable(
                route = Screens.RegisterScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                RegisterScreen(navController = navController, snackbarHostState = snackbarHostState)
            }
            composable(
                route = Screens.GoogleLoginScreen.route,
                enterTransition = { scaleIn(initialScale = 0.8f) + fadeIn() },
                exitTransition = { scaleOut(targetScale = 0.8f) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                GoogleLoginScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(
                route = Screens.HouseScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                HouseScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    viewModel = houseViewModel
                )
            }

            composable(
                route = Screens.CreateHouseScreen.route + "/{uid}",
                arguments = listOf(navArgument("uid") { type = NavType.StringType }),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                CreateHouseScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    uid = uid
                )
            }

            composable(
                route = Screens.RoomScreen.route + "/{houseId}/{houseName}",
                arguments = listOf(
                    navArgument("houseId") { type = NavType.StringType },
                    navArgument("houseName") { type = NavType.StringType }
                ),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                val houseName = backStackEntry.arguments?.getString("houseName") ?: ""
                RoomScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId,
                    houseName = houseName,
                    viewModel = roomViewModel
                )
            }

            composable(
                route = Screens.CreateRoomScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ){ backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                CreateRoomScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId
                )
            }

            composable(
                route = Screens.ServiceScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ){ backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                ServiceScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    viewModelHouse = houseViewModel,
                    houseId = houseId,
                )
            }

            composable(
                route = Screens.InstructionScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                InstructionScreen(navController = navController)
            }

            composable(
                route = Screens.CreateTenantScreen.route + "/{roomId}/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ){ backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                CreateTenantScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    viewModelHouse = houseViewModel,
                    roomId = roomId,
                    houseId = houseId
                )
            }

            composable(
                route = Screens.CreateInvoiceScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->

                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""

                 CreateInvoiceScreen(navController, snackbarHostState, houseId, roomViewModel, invoiceViewModel)
            }

            composable(
                route = Screens.TenantHistoryScreen.route + "/{roomId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                TenantHistoryScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    roomId = roomId,
                    viewModel = roomViewModel
                )
            }

            composable(
                route = Screens.TenantDetailScreen.route + "/{roomId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                TenantDetailScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    roomId = roomId,
                    viewModel = roomViewModel
                )
            }

            composable(
                route = Screens.InvoiceHistoryScreen.route + "/{roomId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                InvoiceHistoryScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    roomId = roomId,
                    viewModel = invoiceViewModel
                )
            }

            composable(
                route = Screens.AcceptInvoiceScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                AcceptInvoiceScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId,
                    viewModelRoom = roomViewModel,
                    viewModelInvoice = invoiceViewModel
                )
            }

            composable(
                route = Screens.InvoiceDetailScreen.route + "/{invoiceId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: ""
                InvoiceDetailScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    invoiceId = invoiceId,
                    viewModel = invoiceViewModel,
                )
            }

            composable(
                route = Screens.ServiceRecordScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                ServiceRecordScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId,
                    viewModelHouse = houseViewModel,
                    viewModelRoom = roomViewModel
                )
            }

            composable(
                route = Screens.CreateExpenseRecordScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                CreateExpenseRecordScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId,
                    viewModelHouse = houseViewModel
                )
            }

            composable(
                route = Screens.ExpenseRecordScreen.route + "/{houseId}",
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
                ExpenseRecordScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    houseId = houseId,
                    viewModelHouse = houseViewModel
                )
            }
        }
    }
}
