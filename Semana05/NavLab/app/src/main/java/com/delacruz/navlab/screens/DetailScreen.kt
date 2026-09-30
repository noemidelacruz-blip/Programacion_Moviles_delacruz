package com.delacruz.navlab.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    StudentDetailScreen(navController = navController, studentId = itemId)
}
