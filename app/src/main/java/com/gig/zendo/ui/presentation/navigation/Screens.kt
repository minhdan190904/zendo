package com.gig.zendo.ui.presentation.navigation

sealed class Screens(val route: String) {
    data object LoginScreen : Screens(route = "LoginScreen")
    data object RegisterScreen : Screens(route = "RegisterScreen")
    data object GoogleLoginScreen : Screens(route = "GoogleLoginScreen")
    data object HouseScreen : Screens(route = "HouseScreen")
    data object CreateHouseScreen : Screens(route = "CreateHouseScreen")
    data object RoomScreen : Screens(route = "RoomScreen")
    data object InstructionScreen : Screens(route = "InstructionScreen")
    data object CreateRoomScreen : Screens(route = "CreateRoomScreen")
    data object ServiceScreen : Screens(route = "ServiceScreen")
    data object CreateTenantScreen : Screens(route = "CreateTenantScreen")
    data object CreateInvoiceScreen : Screens(route = "CreateInvoiceScreen")
    data object TenantHistoryScreen : Screens(route = "TenantHistoryScreen")
    data object TenantDetailScreen : Screens(route = "TenantDetailScreen")
}