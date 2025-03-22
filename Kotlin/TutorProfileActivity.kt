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

class TutorProfileActivity : ComponentActivity() {
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
                    val username = intent.getStringExtra("USERNAME") ?: "Unknown Tutor"
                    TutorProfileScreen(
                        username = username,
                        databaseManager = databaseManager,
                        onBackClick = { navigateToDashboard() },
                        onLogoutClick = { navigateToLoginScreen() }
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

    private fun navigateToLoginScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorProfileScreen(
    username: String,
    databaseManager: DatabaseManager,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var email by remember { mutableStateOf("Loading...") }
    var role by remember { mutableStateOf("Tutor") }
    var studentRequests by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var sessions by remember { mutableStateOf<List<DatabaseManager.SessionEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        databaseManager.getUserDetails(username)?.let { (fetchedUsername, fetchedEmail, fetchedRole) ->
            email = fetchedEmail
            role = fetchedRole
        }
        studentRequests = databaseManager.getStudentRequests(username)
        sessions = databaseManager.getSessionsForUser(username, isTutor = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMint.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        // Top Bar (unchanged)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumSeaGreen)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tutor Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            IconButton(onClick = onLogoutClick) {
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    color = LightSalmon,
                    fontWeight = FontWeight.Bold
                )
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

        // Student Requests (unchanged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Student Requests",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (studentRequests.isEmpty()) {
                    Text(
                        text = "No student requests yet",
                        color = MediumSeaGreen,
                        fontSize = 16.sp
                    )
                } else {
                    studentRequests.forEach { (student, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Student: $student",
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

        // Sessions
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
                                text = "Student: ${session.studentUsername}, Topic: ${session.topic}",
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
fun TutorProfileScreenPreview() {
    BaseConverterTheme {
        TutorProfileScreen(
            username = "TestTutor",
            databaseManager = DatabaseManager(androidx.compose.ui.platform.LocalContext.current),
            onBackClick = {},
            onLogoutClick = {}
        )
    }
}
