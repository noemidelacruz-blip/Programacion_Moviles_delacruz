package com.delacruz.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.navlab.screens.DirectoryScreen
import com.delacruz.navlab.screens.HomeScreen
import com.delacruz.navlab.screens.LoginScreen
import com.delacruz.navlab.screens.ProfileConfigScreen
import com.delacruz.navlab.screens.StudentDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "Minji"
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Minji"
            HomeScreen(navController = navController, userName = userName)
        }
        composable(Screen.Directory.route) {
            DirectoryScreen(navController = navController)
        }
        composable(
            route = Screen.Profile.route
        ) {
            ProfileConfigScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            StudentDetailScreen(navController = navController, studentId = itemId)
        }
    }
}
