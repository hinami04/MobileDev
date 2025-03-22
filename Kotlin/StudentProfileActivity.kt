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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme



class StudentProfileActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this)

        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightMint.copy(alpha = 0.1f)
                ) {
                    val username = intent.getStringExtra("USERNAME") ?: "Unknown Student"
                    StudentProfileScreen(
                        username = username,
                        databaseManager = databaseManager,
                        onBackClick = { navigateToDashboard() },
                        onLogoutClick = { navigateToLoginScreen() },
                        onSettingsClick = { navigateToSettingsScreen() } // Added callback for settings
                    )
                }
            }
        }
    }

    val MintGreen = Color(0xFF98FB98)
    val LightMint = Color(0xFF90EE90)
    val MediumSeaGreen = Color(0xFF3CB371)
    val DarkSeaGreen = Color(0xFF2E8B57)
    val LightSalmon = Color(0xFFFFA07A)

    private fun navigateToDashboard() {
        val intent = Intent(this, StudentDashboardActivity::class.java).apply {
            putExtra("logged_in_user", intent.getStringExtra("USERNAME"))
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, MainActivity::class.java)
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
fun StudentProfileScreen(
    username: String,
    databaseManager: DatabaseManager,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit // Added parameter for settings navigation
) {
    var email by remember { mutableStateOf("Loading...") }
    var role by remember { mutableStateOf("Student") }
    var tutorRequests by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var conversionHistory by remember { mutableStateOf<List<DatabaseManager.ConversionEntry>>(emptyList()) }
    var sessions by remember { mutableStateOf<List<DatabaseManager.SessionEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        databaseManager.getUserDetails(username)?.let { (fetchedUsername, fetchedEmail, fetchedRole) ->
            email = fetchedEmail
            role = fetchedRole
        }
        tutorRequests = databaseManager.getTutorRequestsForStudent(username)
        conversionHistory = databaseManager.getConversionHistory(username)
        sessions = databaseManager.getSessionsForUser(username, isTutor = false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMint.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        // Top Bar (Updated to include Settings button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumSeaGreen)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Student Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
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
                IconButton(onClick = onLogoutClick) {
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        color = LightSalmon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card (unchanged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(DarkSeaGreen, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.first().toString().uppercase(),
                        fontSize = 40.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = username,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = email,
                    fontSize = 18.sp,
                    color = MediumSeaGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Role: $role",
                    fontSize = 18.sp,
                    color = MediumSeaGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tutor Requests (unchanged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tutor Requests",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (tutorRequests.isEmpty()) {
                    Text(
                        text = "No tutor requests made yet",
                        color = MediumSeaGreen,
                        fontSize = 16.sp
                    )
                } else {
                    tutorRequests.forEach { (tutor, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tutor: $tutor",
                                fontSize = 16.sp,
                                color = DarkSeaGreen
                            )
                            Text(
                                text = "Status: $status",
                                fontSize = 16.sp,
                                color = MediumSeaGreen
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conversion History (unchanged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Conversion History",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (conversionHistory.isEmpty()) {
                    Text(
                        text = "No conversions yet",
                        color = MediumSeaGreen,
                        fontSize = 16.sp
                    )
                } else {
                    conversionHistory.take(5).forEach { conversion ->
                        Text(
                            text = "${conversion.inputValue} (Base ${conversion.inputBase}) -> ${conversion.outputValue} (Base ${conversion.outputBase})",
                            fontSize = 16.sp,
                            color = MediumSeaGreen,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sessions (unchanged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tutoring Sessions",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (sessions.isEmpty()) {
                    Text(
                        text = "No sessions scheduled",
                        color = MediumSeaGreen,
                        fontSize = 16.sp
                    )
                } else {
                    sessions.forEach { session ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tutor: ${session.tutorUsername}, Topic: ${session.topic}",
                                fontSize = 16.sp,
                                color = DarkSeaGreen
                            )
                            Text(
                                text = "Status: ${session.status}",
                                fontSize = 16.sp,
                                color = MediumSeaGreen
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Back to Dashboard Button (unchanged)
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Back to Dashboard",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentProfileScreenPreview() {
    BaseConverterTheme {
        StudentProfileScreen(
            username = "TestStudent",
            databaseManager = DatabaseManager(androidx.compose.ui.platform.LocalContext.current),
            onBackClick = {},
            onLogoutClick = {},
            onSettingsClick = {} // Added for preview
        )
    }
}
