package com.flavor.trail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flavor.trail.ui.chat.ChatScreen
import com.flavor.trail.ui.map.MapScreen
import com.flavor.trail.ui.profile.ProfileScreen

object Routes {
    const val MAP = "map"
    const val CHAT = "chat"
    const val PROFILE = "profile"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Surface(modifier = Modifier) {
            NavHost(navController = navController, startDestination = Routes.MAP) {
                composable(Routes.MAP) {
                    MapScreen(modifier = Modifier.padding(innerPadding))
                }
                composable(Routes.CHAT) {
                    ChatScreen(modifier = Modifier.padding(innerPadding))
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(Routes.MAP, "首页", Icons.Map),
        BottomNavItem(Routes.CHAT, "对话", Icons.Chat),
        BottomNavItem(Routes.PROFILE, "我的", Icons.Profile),
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)