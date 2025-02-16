package com.sidv.boundservicemessenger.navigation


import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.sidv.boundservicemessenger.common.NavigationIcons

@Composable
fun CustomNavigationBar(navController: NavController, selectedRoute: String) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        // For You
        NavigationBarItem(
            icon = {
                // Đổi icon khi mục được chọn
                val icon = if (selectedRoute == "page_one") {
                    painterResource(NavigationIcons.PAGE_ONE)
                } else {
                    painterResource(NavigationIcons.PAGE_ONE)
                }
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            },
            label = { Text("Page One") },
            selected = selectedRoute == "page_one",
            onClick = { navController.navigate("page_one") }
        )

        // Interests
        NavigationBarItem(
            icon = {
                val icon = if (selectedRoute == "page_two") {
                    painterResource(NavigationIcons.PAGE_TWO) // Icon khi được chọn
                } else {
                    painterResource(NavigationIcons.PAGE_TWO) // Icon khi chưa được chọn
                }
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            },
            label = { Text("Page Two") },
            selected = selectedRoute == "page_two",
            onClick = { navController.navigate("page_two") }
        )
        // Interests
        NavigationBarItem(
            icon = {
                val icon = if (selectedRoute == "page_three") {
                    painterResource(NavigationIcons.PAGE_THREE) // Icon khi được chọn
                } else {
                    painterResource(NavigationIcons.PAGE_THREE) // Icon khi chưa được chọn
                }
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            },
            label = { Text("Page Three") },
            selected = selectedRoute == "page_three",
            onClick = { navController.navigate("page_three") }
        )
    }
}
