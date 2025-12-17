package com.example.fr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fr.viewmodel.FontViewModel

data class FontScale(val name: String, val scale: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFontScreen(navController: NavController, fontViewModel: FontViewModel = viewModel()) {
    val fontScales = listOf(
        FontScale("Small", 0.85f),
        FontScale("Medium", 1.0f),
        FontScale("Large", 1.15f)
    )
    val selectedFontScale by fontViewModel.fontScale.collectAsState()
    var tempFontScale by remember { mutableStateOf(selectedFontScale) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Font") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Font Size", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(fontScales) { fontScale ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tempFontScale == fontScale,
                            onClick = { tempFontScale = fontScale }
                        )
                        Text(text = fontScale.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { fontViewModel.saveFontScale(tempFontScale) }) {
                Text("Save")
            }
        }
    }
}