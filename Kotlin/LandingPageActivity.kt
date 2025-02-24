package com.example.baseconvert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconvert.ui.theme.BaseConvertTheme
import java.util.*

// Custom colors from your theme
val MintGreen = Color(0xFF98FB98)
val LightMint = Color(0xFF90EE90)
val MediumSeaGreen = Color(0xFF3CB371)
val DarkGreen = Color(0xFF0B5345)

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConvertTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val username = intent.getStringExtra("logged_in_user") ?: "User"
                    LandingPage(
                        username = username,
                        onProfileClick = { navigateToProfileScreen() },
                        onSettingsClick = { navigateToSettingsScreen() }
                    )
                }
            }
        }
    }

    private fun navigateToProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(username: String, onProfileClick: () -> Unit, onSettingsClick: () -> Unit) {
    // Determine the time-based greeting
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$greeting, $username", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(onSettingsClick = onSettingsClick)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MintGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)), // Rounded corners
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Welcome to BaseConvert",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = DarkGreen,
                                        fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Your one-stop solution for all conversion needs",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MediumSeaGreen
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = onProfileClick,
                            modifier = Modifier.padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkGreen
                            )
                        ) {
                            Text(
                                text = "Go to Profile",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }


            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(LightMint.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)), // Rounded corners
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Calculator",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = DarkGreen
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Convert from decimal to hexadecimal!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MediumSeaGreen
                            ),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { /* Navigate to calculator */ },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkGreen
                            )
                        ) {
                            Text(
                                text = "Start Converting",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .background(LightMint.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Daily Challenges",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = DarkGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Test your skills!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MediumSeaGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .background(LightMint.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Testimonials",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = DarkGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "\"Best conversion app!\"",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MediumSeaGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .background(LightMint.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "History",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = DarkGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Recent conversions",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MediumSeaGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .background(LightMint.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Contact Us",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = DarkGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "support@baseconvert.com",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MediumSeaGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(onSettingsClick: () -> Unit) {
    NavigationBar(
        containerColor = DarkGreen
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Home", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White) },
            label = { Text("Settings", color = Color.White) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    BaseConvertTheme {
        LandingPage(username = "User", {}, {})
    }
}
