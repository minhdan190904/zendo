package com.gig.zendo.ui.presentation.navigation

sealed class Screens(val route: String) {
    data object LoginScreen : Screens(route = "LoginScreen")
    data object RegisterScreen : Screens(route = "RegisterScreen")
    data object GoogleLoginScreen : Screens(route = "GoogleLoginScreen")
    data object HouseScreen : Screens(route = "HouseScreen")
    data object CreateHouseScreen : Screens(route = "CreateHouseScreen")
    data object SampleScreen : Screens(route = "SampleScreen")
}