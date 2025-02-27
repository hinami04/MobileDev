package com.example.baseconvert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.baseconvert.ui.theme.BaseConvertTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this) // Initialize the database manager

        setContent {
            BaseConvertTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF98FB98) // Pale Green
                ) {
                    LoginScreen(
                        onRegisterClick = { navigateToRegisterScreen() },
                        onLoginSuccess = { username -> navigateToLandingPage(username) } // Updated to pass username
                    )
                }
            }
        }
    }

    private fun navigateToRegisterScreen() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLandingPage(username: String) {
        val intent = Intent(this, LandingPageActivity::class.java)
        intent.putExtra("logged_in_user", username) // Pass the username to LandingPageActivity
        startActivity(intent)
    }

    @Composable
    fun LoginScreen(onRegisterClick: () -> Unit, onLoginSuccess: (String) -> Unit) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loginMessage by remember { mutableStateOf("") }
        var isUsernameValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }

        val scope = rememberCoroutineScope() // For database operations

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF58d68d)), // Light Green
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF0B5345)
                ) // Sea Green

                Spacer(modifier = Modifier.height(24.dp))

                // Username Label
                Text(
                    text = "Username",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF0B5345),
                    modifier = Modifier.fillMaxWidth() // Ensure it fills the width for alignment
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Username Field
                BasicTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isUsernameValid = username.isNotEmpty()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isUsernameValid) Color.LightGray else Color(0xFFFFA07A)) // Light Green or Light Salmon for invalid input
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (username.isEmpty()) {
                            Text(
                                text = "Enter Username",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )
                if (!isUsernameValid) {
                    Text(text = "Username cannot be empty", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Label
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF0B5345),
                    modifier = Modifier.fillMaxWidth() // Ensure it fills the width for alignment
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password Field
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordValid = password.length >= 6
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPasswordValid) Color.LightGray else Color(0xFFFFA07A)) // Light Green or Light Salmon for invalid input
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (password.isEmpty()) {
                            Text(
                                text = "Enter Password",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )
                if (!isPasswordValid) {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isUsernameValid && isPasswordValid) {
                            scope.launch {
                                val success = databaseManager.loginUser(username, password)
                                if (success) {
                                    onLoginSuccess(username) // Pass the username to the callback
                                } else {
                                    loginMessage = "Invalid login credentials"
                                }
                            }
                        } else {
                            loginMessage = "Please fill in all fields correctly"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B5345)), // Sea Green
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Login", fontSize = 18.sp, color = Color.White) // Pale Green
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have an account? Register here",
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onRegisterClick() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = loginMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (loginMessage.startsWith("Invalid")) Color.Red else Color.Green
                )
            }
        }
    }
}

    @Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BaseConvertTheme {
        MainActivity().LoginScreen({}, { _ -> }) // Updated to match the new callback signature
    }
}
