package com.example.baseconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.baseconvert.ui.theme.BaseConvertTheme

//LightYellow = Color(0xFFFFF5E4)
//val Maroon = Color(0xFF660000)
//val LightRed = Color(0xFFFFA8A8)

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConvertTheme(darkTheme = true) {
                HistoryScreen(
                    onBackClick = { finish() }, // Calls finish() to close the activity
                    databaseManager = DatabaseManager()
                )
            }
        }
    }

    private fun DatabaseManager(): DatabaseManager {
        return DatabaseManager(this)
    }
}

@Composable
fun HistoryScreen(onBackClick: () -> Unit, databaseManager: DatabaseManager) {
    val historyList = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        // Load history from the database
        val historyFromDB = databaseManager.getConversionHistory()
        historyList.clear()
        historyList.addAll(historyFromDB)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "History",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Button(onClick = {
                databaseManager.clearConversionHistory()
                historyList.clear()
            }) {
                Text("Delete All")
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between the top bar and the list

        // History List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(historyList) { item ->
                HistoryItem(text = item)
            }
        }
    }
}

@Composable
fun HistoryItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    // Use a simple mock database manager for the preview
    HistoryScreen(
        onBackClick = {}, // Mock back click for preview
        databaseManager = DatabaseManager()
    )
}

fun DatabaseManager(): DatabaseManager {
     return DatabaseManager()
}

