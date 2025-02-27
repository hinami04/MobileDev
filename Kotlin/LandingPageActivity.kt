package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme
import java.util.*

// Custom colors (unchanged)
val MintGreen = Color(0xFF98FB98)
val LightMint = Color(0xFF90EE90)
val MediumSeaGreen = Color(0xFF3CB371)
val DarkSeaGreen = Color(0xFF2E8B57)
val LightSalmon = Color(0xFFFFA07A)

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
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
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navigate to calculator */ },
                containerColor = MediumSeaGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(64.dp)
                    .shadow(8.dp, CircleShape)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Conversion")
            }
        },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MediumSeaGreen)
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$greeting, $username",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightMint.copy(alpha = 0.1f)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MintGreen.copy(alpha = 0.3f))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BaseConvert",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Simplify your number conversions",
                            fontSize = 16.sp,
                            color = MediumSeaGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        title = "Recent",
                        subtitle = "View history",
                        color = DarkSeaGreen.copy(alpha = 0.2f),
                        onClick = { /* Navigate to history */ },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Profile",
                        subtitle = "Your stats",
                        color = DarkSeaGreen.copy(alpha = 0.2f),
                        onClick = onProfileClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Features Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Features",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        FeatureItem("Instant Conversions", "Real-time base conversion")
                        FeatureItem("Multiple Bases", "Support for 2-36 bases")
                        FeatureItem("History Tracking", "Save your conversions")
                    }
                }
            }

            // Stats/Testimonial
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Loved by Users",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen
                        )
                        Text(
                            text = "\"Fastest converter I've used!\"",
                            fontSize = 16.sp,
                            color = MediumSeaGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSeaGreen
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MediumSeaGreen
            )
        }
    }
}

@Composable
fun FeatureItem(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(MediumSeaGreen, CircleShape)
        )
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkSeaGreen
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = MediumSeaGreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    BaseConverterTheme {
        LandingPage(username = "User", {}, {})
    }
}
