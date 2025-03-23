package com.example.baseconverter

import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme

// New color scheme
val Maroon = Color(0xFF850E35)
val LightPink = Color(0xFFFFC4C4)
val Pink = Color(0xFFEE6983)
val LightYellow = Color(0xFFFFF5E4)

class StudentDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseManager = DatabaseManager(this)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightPink // Updated background color
                ) {
                    val username = intent.getStringExtra("logged_in_user") ?: "Student"
                    StudentDashboard(
                        username = username,
                        onProfileClick = { navigateToProfileScreen(username) },
                        onBaseConvertClick = { navigateToBaseConverter() },
                        onRequestTutorClick = { tutorUsername ->
                            val success = databaseManager.requestTutor(username, tutorUsername)
                            // Optionally show a toast or update UI based on success
                        }
                    )
                }
            }
        }
    }

    private fun navigateToProfileScreen(username: String) {
        val intent = Intent(this, StudentProfileActivity::class.java).apply {
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
fun StudentDashboard(
    username: String,
    onProfileClick: () -> Unit,
    onBaseConvertClick: () -> Unit,
    onRequestTutorClick: (String) -> Unit
) {
    val context = LocalContext.current
    val databaseManager = remember { DatabaseManager(context) }
    var tutors by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        tutors = databaseManager.getAvailableTutors()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPink) // Updated background color
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
            IconButton(onClick = onProfileClick) {
                Text(
                    text = "Profile",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
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
            }
        }

        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Find Your Teacher Section
            item {
                Text(
                    text = "Find your teacher!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pink, // Updated to Pink
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Category Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryButton(text = "+ x /", onClick = { /* Math category action */ })
                    CategoryButton(text = "ABC", onClick = { /* Alphabet category action */ })
                    CategoryButton(text = "0101", onClick = onBaseConvertClick)
                    CategoryButton(text = "More", onClick = { /* More categories action */ })
                }
            }

            // Top Teachers Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightYellow) // Updated to LightYellow
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Top Teachers",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Pink, // Updated to Pink
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (tutors.isEmpty()) {
                            Text(
                                text = "No teachers available",
                                color = DarkGray, // Updated to DarkGray
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }

            // List of Teachers
            items(tutors) { tutor ->
                TeacherCard(
                    name = tutor,
                    onRequestClick = { onRequestTutorClick(tutor) },
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}

@Composable
fun CategoryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Maroon) // Updated to Maroon
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TeacherCard(name: String, onRequestClick: () -> Unit, onProfileClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightYellow) // Updated to LightYellow
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onProfileClick)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Pink, shape = CircleShape), // Updated to Pink
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.first().toString().uppercase(),
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkGray // Updated to DarkGray
                )
            }
            Button(
                onClick = onRequestClick,
                colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "Request",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentDashboardPreview() {
    BaseConverterTheme {
        StudentDashboard(
            username = "StudentName",
            onProfileClick = {},
            onBaseConvertClick = {},
            onRequestTutorClick = {}
        )
    }
}
