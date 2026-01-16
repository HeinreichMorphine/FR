package com.example.fr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fr.ui.theme.RescuePrimary

data class NewsItem(
    val title: String,
    val date: String,
    val summary: String,
    val source: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController) {
    val mockNews = listOf(
        NewsItem(
            title = "Flood Warning Issued for East Coast",
            date = "2024-12-25 08:00 AM",
            summary = "Heavy rainfall expected over the next 48 hours. Residents in low-lying areas are advised to evacuate immediately.",
            source = "MetMalaysia"
        ),
        NewsItem(
            title = "New Relief Centers Opened",
            date = "2024-12-24 06:30 PM",
            summary = "Three new evacuation centers have been opened in the Hulu Langat district to accommodate displaced families.",
            source = "NADMA"
        ),
        NewsItem(
            title = "Water Levels Receding in Some Areas",
            date = "2024-12-24 02:00 PM",
            summary = "Authorities report that floodwaters are slowly receding in urban areas, but caution is still advised.",
            source = "Local News"
        ),
        NewsItem(
            title = "Emergency Hotline Numbers Updated",
            date = "2024-12-23 10:00 AM",
            summary = "Please save these new emergency contact numbers for flood rescue operations: 999 (General), 1-800-88-1234 (Flood Ops).",
            source = "Civil Defence Force"
        ),
        NewsItem(
            title = "Community Cleanup Drive This Weekend",
            date = "2024-12-22 09:00 AM",
            summary = "Volunteers are needed for post-flood cleanup activities in affected neighborhoods. Sign up at the town hall.",
            source = "Community Watch"
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("News") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockNews) { news ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = news.source,
                                style = MaterialTheme.typography.labelMedium,
                                color = RescuePrimary
                            )
                            Text(
                                text = news.date,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = news.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}