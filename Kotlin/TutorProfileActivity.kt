package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme



class TutorProfileActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this)

        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightPink // Updated to LightPink
                ) {
                    val username = intent.getStringExtra("USERNAME") ?: "Unknown Tutor"
                    TutorProfileScreen(
                        username = username,
                        databaseManager = databaseManager,
                        onBackClick = { navigateToDashboard() },
                        onMenuClick = { /* Placeholder for menu action */ },
                        onSettingsClick = { navigateToSettingsScreen() }
                    )
                }
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, TutorDashboardActivity::class.java).apply {
            putExtra("logged_in_user", intent.getStringExtra("USERNAME"))
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorProfileScreen(
    username: String,
    databaseManager: DatabaseManager,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var email by remember { mutableStateOf("Loading...") }
    var role by remember { mutableStateOf("Tutor") }

    LaunchedEffect(Unit) {
        databaseManager.getUserDetails(username)?.let { (fetchedUsername, fetchedEmail, fetchedRole) ->
            email = fetchedEmail
            role = fetchedRole
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPink) // Updated to LightPink
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Maroon) // Updated to Maroon
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSettingsClick) {
                    Text(
                        text = "Settings",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onMenuClick) {
                    Text(
                        text = "Menu",
                        fontSize = 16.sp,
                        color = Pink, // Updated to Pink
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Pink, shape = CircleShape), // Updated to Pink
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.first().toString().uppercase(),
                    fontSize = 48.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = username,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray, // Updated to DarkGray
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Information Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightYellow) // Updated to LightYellow
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Information",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pink, // Updated to Pink
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Credentials
                Text(
                    text = "Credentials",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkGray, // Updated to DarkGray
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp), // Increased height for better readability
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Tutor: $username",
                            fontSize = 16.sp,
                            color = DarkGray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "BaseConverter University",
                            fontSize = 14.sp,
                            color = Pink,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Subjects
                Text(
                    text = "Subjects",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkGray, // Updated to DarkGray
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Mathematics, Computer Science", // Placeholder text
                            fontSize = 16.sp,
                            color = DarkGray
                        )
                    }
                }

                // Contact Info
                Text(
                    text = "Contact Info",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkGray, // Updated to DarkGray
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = email,
                            fontSize = 16.sp,
                            color = DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TutorProfileScreenPreview() {
    BaseConverterTheme {
        TutorProfileScreen(
            username = "TestTutor",
            databaseManager = DatabaseManager(androidx.compose.ui.platform.LocalContext.current),
            onBackClick = {},
            onMenuClick = {},
            onSettingsClick = {}
        )
    }
}
