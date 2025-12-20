package com.example.fr.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class NewsItem(val title: String, val date: String, val time: String, val content: String)

@Composable
fun NewsScreen(navController: androidx.navigation.NavController) {
    val newsList = listOf(
        NewsItem("Heavy Rain Alert", "2024-12-20", "08:00 AM", "Continuous heavy rain expected in KL area."),
        NewsItem("Shelter Opened", "2024-12-19", "06:30 PM", "New relief center opened at Merdeka Hall."),
        NewsItem("Flash Flood Warning", "2024-12-18", "02:15 PM", "Low lying areas advised to evacuate.")
    )

    com.example.fr.ui.components.AppScaffold(title = "News", navController = navController) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text(text = "Latest News", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(newsList) { news ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = news.title, style = MaterialTheme.typography.titleMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = news.date, style = MaterialTheme.typography.bodySmall)
                            Text(text = news.time, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = news.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
