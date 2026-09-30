package com.delacruz.navlab.navigation

sealed class Screen(val route: String) {

    object Login : Screen(route = "login")

    object Home : Screen(route = "home/{userName}") {
        fun createRoute(userName: String): String {
            val name = if (userName.isBlank()) "Minji" else userName.trim()
            return "home/$name"
        }
    }

    object Directory : Screen(route = "directory")
    object List : Screen(route = "directory")

    object Profile : Screen(route = "profile")
    object ProfileConfig : Screen(route = "profile")

    object Detail : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
    object StudentDetail : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}
