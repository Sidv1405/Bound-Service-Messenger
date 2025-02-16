package com.sidv.boundservicemessenger.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sidv.boundservicemessenger.screen.PageOneScreen
import com.sidv.boundservicebinder.screen.PageThreeScreen
import com.sidv.boundservicemessenger.screen.PageTwoScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry.value?.destination?.route) {
        // Optional: handle any effects when the route changes
    }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                navController = navController,
                selectedRoute = currentBackStackEntry.value?.destination?.route ?: "page_one"
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "page_one",
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable("page_one") { PageOneScreen() }
            composable("page_two") { PageTwoScreen() }
            composable("page_three") { PageThreeScreen() }
        }
    }
}