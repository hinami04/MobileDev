package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.baseconverter.ui.theme.BaseConverterTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen { navigateToDeveloperScreen() }
                }
            }
        }
    }

    private fun navigateToDeveloperScreen() {
        val intent = Intent(this, DeveloperActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun SettingsScreen(onAboutDeveloperClick: () -> Unit) {
    var isDarkThemeEnabled by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(LightMint.copy(alpha = 0.1f)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkSeaGreen,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Preferences Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    color = MediumSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Dark Theme Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Dark Theme",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkSeaGreen
                    )
                    Switch(
                        checked = isDarkThemeEnabled,
                        onCheckedChange = { isDarkThemeEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MediumSeaGreen,
                            checkedTrackColor = MediumSeaGreen.copy(alpha = 0.5f),
                            uncheckedThumbColor = LightSalmon,
                            uncheckedTrackColor = LightSalmon.copy(alpha = 0.5f)
                        )
                    )
                }

                // Notifications Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Enable Notifications",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkSeaGreen
                    )
                    Switch(
                        checked = isNotificationsEnabled,
                        onCheckedChange = { isNotificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MediumSeaGreen,
                            checkedTrackColor = MediumSeaGreen.copy(alpha = 0.5f),
                            uncheckedThumbColor = LightSalmon,
                            uncheckedTrackColor = LightSalmon.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // About Developer Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About Developer",
                    style = MaterialTheme.typography.titleMedium,
                    color = MediumSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Learn more about the developer behind this app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = onAboutDeveloperClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "View Developer Info",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BaseConverterTheme {
        SettingsScreen {}
    }
}
