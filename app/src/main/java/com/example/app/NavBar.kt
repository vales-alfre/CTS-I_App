package com.example.app
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app.R

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("perfil", R.drawable.baseline_account_box_24, "Perfil"),
        BottomNavItem("lista", R.drawable.list_alt, "Lista")
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(containerColor = Color(0xFFEFF6F7)) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0099A8),
                    unselectedIconColor = Color(0xFF537275),
                    indicatorColor = Color(0xFF63E8F5)
                )
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: Int, val label: String)
