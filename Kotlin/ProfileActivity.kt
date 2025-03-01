package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme

class ProfileActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this)

        // Get username from intent (passed from login screen)
        val username = intent.getStringExtra("USERNAME") ?: "Unknown"

        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen(
                        username = username,
                        databaseManager = databaseManager,
                        onLogoutConfirmed = { navigateToLoginScreen() },
                        onSettingsClick = { navigateToSettingsScreen() }
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

    private fun navigateToSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ProfileScreen(
    username: String,
    databaseManager: DatabaseManager,
    onLogoutConfirmed: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Color definitions
    val MintGreen = Color(0xFF98FB98)
    val LightMint = Color(0xFF90EE90)
    val MediumSeaGreen = Color(0xFF3CB371)
    val DarkSeaGreen = Color(0xFF2E8B57)
    val LightSalmon = Color(0xFFFFA07A)

    // Load initial user data
    var userEmail by remember { mutableStateOf("") }
    var userUsername by remember { mutableStateOf(username) }
    var password by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var debugMessage by remember { mutableStateOf("Loading user data...") }
    // Load user data when screen is first composed

    LaunchedEffect(username) {
        println("ProfileScreen: Fetching details for username = $username")
        databaseManager.getUserDetails(username)?.let { (dbUsername, email) ->
            println("ProfileScreen: Found user - Username: $dbUsername, Email: $email")
            userUsername = dbUsername
            userEmail = email
            debugMessage = "User data loaded successfully"
        } ?: run {
            println("ProfileScreen: No user details found for $username")
            debugMessage = "No user found for $username"
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = LightMint,
            title = {
                Text(
                    "Logout Confirmation",
                    color = DarkSeaGreen,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = { Text("Are you sure you want to logout?", color = MediumSeaGreen) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutConfirmed()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = DarkSeaGreen)
                ) { Text("Yes") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = LightSalmon)
                ) { Text("No") }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MintGreen)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkSeaGreen,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = debugMessage,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(LightMint)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isEditing) {
                    OutlinedTextField(
                        value = userUsername,
                        onValueChange = { userUsername = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MediumSeaGreen,
                            unfocusedBorderColor = DarkSeaGreen
                        )
                    )
                    OutlinedTextField(
                        value = userEmail,
                        onValueChange = { userEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MediumSeaGreen,
                            unfocusedBorderColor = DarkSeaGreen
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MediumSeaGreen,
                            unfocusedBorderColor = DarkSeaGreen
                        )
                    )


                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Changes", fontSize = 16.sp)
                    }
                } else {
                    ProfileInfoRow(label = "Username", value = userUsername)
                    ProfileInfoRow(label = "Email", value = userEmail)
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Edit Profile", fontSize = 16.sp)
                    }
                }
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onSettingsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkSeaGreen),
                border = BorderStroke(1.dp, DarkSeaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Settings", fontSize = 16.sp)
            }

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LightSalmon, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BaseConverterTheme {
        ProfileScreen(
            username = "preview_user",
            databaseManager = DatabaseManager(LocalContext.current),
            onLogoutConfirmed = {},
            onSettingsClick = {}
        )
    }
}
