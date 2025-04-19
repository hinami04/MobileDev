package com.example.baseconvert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconvert.ui.theme.BaseConvertTheme

//LightYellow = Color(0xFFFFF5E4)
//val Maroon = Color(0xFF660000)
//val LightRed = Color(0xFFFFA8A8)

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkThemeEnabled = true // Adjust this flag dynamically if needed
            BaseConvertTheme(darkTheme = isDarkThemeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Adapt to theme background color
                ) {
                    ProfileScreen(
                        onLogoutConfirmed = { navigateToLoginScreen() },
                        onBackClick = { navigateToLandingPage() },
                        onNotesClick = { navigateToNotesPage() },
                        onHistoryClick = { navigateToHistoryPage() }
                    )
                }
            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLandingPage() {
        val intent = Intent(this, LandingPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHistoryPage(){
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToNotesPage(){
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutConfirmed: () -> Unit,
    onBackClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    var username by remember { mutableStateOf("john_doe") }
    var email by remember { mutableStateOf("john_doe@example.com") }
    var isEditing by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout Confirmation") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutConfirmed()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = username, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle settings click */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF660000))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF5E4))
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.x1), // Replace with your drawable resource
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF660000)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF660000))
                ) {
                    Text(text = "Edit Profile", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rounded corner boxes for 'Notes' and 'History'
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFA8A8).copy(alpha = 0.4f))
                        .clickable(onClick = { onNotesClick() }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Notes", style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFA8A8).copy(alpha = 0.4f))
                        .clickable(onClick = { onHistoryClick() }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "History", style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onLogoutConfirmed = {},
        onBackClick = {},
        onNotesClick = {},
        onHistoryClick = {}
    )
}
