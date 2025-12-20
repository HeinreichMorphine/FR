package com.example.fr.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(navController: androidx.navigation.NavController) {
    val uriHandler = LocalUriHandler.current

    com.example.fr.ui.components.AppScaffold(title = "About", navController = navController) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "About FloodRescue", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "FloodRescue Application v1.0",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Visit our website",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://example.com/floodrescue")
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Copyright © 2024 FloodRescue Team", style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Group Members:", style = MaterialTheme.typography.titleMedium)
            Text(text = "- Member 1 (TBD)")
            Text(text = "- Member 2 (TBD)")
        }
    }
}