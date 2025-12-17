package com.example.fr.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fr.ui.model.BottomNavItem
import com.example.fr.viewmodel.ProfileViewModel

@Composable
fun MainScreen(name: String) {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()
    val bottomNavItems = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Profile", "profile", Icons.Default.AccountCircle),
        BottomNavItem("Settings", "settings", Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                        label = { Text(text = item.name) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(name = name) }
                composable("profile") { ProfileScreen(profileViewModel = profileViewModel) }
                composable("settings") { SettingsScreen(navController = navController) }
                composable("edit_profile") { EditProfileScreen(navController = navController, profileViewModel = profileViewModel) }
                composable("location_history") { LocationHistoryScreen(navController = navController) }
            }
        }
    }
}