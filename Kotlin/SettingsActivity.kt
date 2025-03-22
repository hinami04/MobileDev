package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme



class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightMint.copy(alpha = 0.1f)
                ) {
                    SettingsScreen(
                        navigateToPreviousPage = { finish() },
                        navigateToDeveloperScreen = { navigateToDeveloperScreen() }
                    )
                }
            }
        }
    }

    private fun navigateToDeveloperScreen() {
        val intent = Intent(this, DeveloperActivity::class.java)
        startActivity(intent)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateToPreviousPage: () -> Unit,
    navigateToDeveloperScreen: () -> Unit
) {
    var isDarkThemeEnabled by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf("Medium") }

    val MintGreen = Color(0xFF98FB98)
    val LightMint = Color(0xFF90EE90)
    val MediumSeaGreen = Color(0xFF3CB371)
    val DarkSeaGreen = Color(0xFF2E8B57)
    val LightSalmon = Color(0xFFFFA07A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMint.copy(alpha = 0.1f))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumSeaGreen)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            // You can add a search icon here if needed, as in the new code
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Preferences Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Preferences",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
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
                                fontSize = 16.sp,
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
                                fontSize = 16.sp,
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

                        // Font Size (from new code)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Font Size",
                                fontSize = 16.sp,
                                color = DarkSeaGreen
                            )
                            Text(
                                text = fontSize,
                                fontSize = 16.sp,
                                color = MediumSeaGreen
                            )
                        }
                    }
                }
            }

            // About Developer Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About Developer",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Learn more about the developer behind this app.",
                            fontSize = 16.sp,
                            color = DarkSeaGreen,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = navigateToDeveloperScreen,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "View Developer Info",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            // Back to Previous Page Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = navigateToPreviousPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontSize = 18.sp
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
        SettingsScreen(
            navigateToPreviousPage = {},
            navigateToDeveloperScreen = {}
        )
    }
}
