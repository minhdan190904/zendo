package com.gig.zendo.ui.presentation.navigation

import HouseScreen
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gig.zendo.ui.presentation.auth.login.GoogleLoginScreen
import com.gig.zendo.ui.presentation.auth.login.LoginScreen
import com.gig.zendo.ui.presentation.auth.register.RegisterScreen
import com.gig.zendo.ui.presentation.home.CreateHouseScreen
import com.gig.zendo.ui.presentation.instruction.InstructionScreen
import com.gig.zendo.ui.presentation.room.CreateRoomScreen
import com.gig.zendo.ui.presentation.room.RoomScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

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
                    snackbarHostState = snackbarHostState
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
                    houseName = houseName
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
                route = Screens.InstructionScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                InstructionScreen(navController = navController)
            }
        }
    }
}
