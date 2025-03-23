package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme
import java.util.Calendar

class TutorDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightPink // Updated to LightPink
                ) {
                    val username = intent.getStringExtra("logged_in_user") ?: "Tutor"
                    TutorDashboard(
                        username = username,
                        onProfileClick = { navigateToProfileScreen(username) },
                        onBaseConvertClick = { navigateToBaseConverter() }
                    )
                }
            }
        }
    }

    private fun navigateToProfileScreen(username: String) {
        val intent = Intent(this, TutorProfileActivity::class.java).apply {
            putExtra("USERNAME", username)
        }
        startActivity(intent)
    }

    private fun navigateToBaseConverter() {
        val intent = Intent(this, BaseConverterActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorDashboard(
    username: String,
    onProfileClick: () -> Unit,
    onBaseConvertClick: () -> Unit
) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPink) // Updated to LightPink
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Maroon) // Updated to Maroon
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$greeting, $username",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* Search action */ }) {
                    Text(
                        text = "Search",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { /* Notifications action */ }) {
                    Text(
                        text = "Notifications",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onProfileClick) {
                    Text(
                        text = "Profile",
                        fontSize = 16.sp,
                        color = Pink, // Updated to Pink
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Welcome Message
        Text(
            text = "Tutor Dashboard",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Pink, // Updated to Pink
            textAlign = TextAlign.Center
        )
        Text(
            text = "Support Your Students",
            fontSize = 18.sp,
            color = DarkGray, // Updated to DarkGray
            textAlign = TextAlign.Center
        )

        // Quick Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onProfileClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Profile", color = Color.White, fontSize = 16.sp)
            }
            Button(
                onClick = onBaseConvertClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Base Converter", color = Color.White, fontSize = 16.sp)
            }
        }

        // Student Management Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightYellow) // Updated to LightYellow
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Student Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pink // Updated to Pink
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Placeholder student list
                val students = listOf("Student John", "Student Emma")
                if (students.isEmpty()) {
                    Text(
                        text = "No requests yet",
                        fontSize = 16.sp,
                        color = DarkGray // Updated to DarkGray
                    )
                } else {
                    students.forEach { student ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = student,
                                fontSize = 16.sp,
                                color = DarkGray // Updated to DarkGray
                            )
                            Button(
                                onClick = { /* Manage student action */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Manage", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        // Spacer to push content up if needed
        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Text(
            text = "© 2025 Base Converter",
            fontSize = 12.sp,
            color = DarkGray, // Updated to DarkGray
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TutorDashboardPreview() {
    BaseConverterTheme {
        TutorDashboard(
            username = "TutorName",
            onProfileClick = {},
            onBaseConvertClick = {}
        )
    }
}
