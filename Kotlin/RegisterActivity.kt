package com.example.baseconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.baseconverter.ui.theme.BaseConverterTheme
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this) // Initialize the database manager

        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen()
                }
            }
        }
    }

    @Composable
    fun RegisterScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") } // New state for confirm password
        var email by remember { mutableStateOf("") }
        var registerMessage by remember { mutableStateOf("") }
        var isUsernameValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isConfirmPasswordValid by remember { mutableStateOf(true) } // New state for confirm password validation
        var isEmailValid by remember { mutableStateOf(true) }

        val scope = rememberCoroutineScope() // For database operations

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Register", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(24.dp))

                // Username Field with Label
                Column {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                    )
                    BasicTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            isUsernameValid = username.isNotEmpty()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isUsernameValid) Color.LightGray else Color.Red.copy(alpha = 0.3f))
                            .padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (username.isEmpty()) {
                                Text(
                                    text = "Enter username",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (!isUsernameValid) {
                        Text(text = "Username cannot be empty", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field with Label
                Column {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                    )
                    BasicTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            isPasswordValid = password.length >= 6
                            isConfirmPasswordValid = confirmPassword.isEmpty() || confirmPassword == password // Update confirm password validation
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isPasswordValid) Color.LightGray else Color.Red.copy(alpha = 0.3f))
                            .padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (password.isEmpty()) {
                                Text(
                                    text = "Enter password",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (!isPasswordValid) {
                        Text(text = "Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field with Label
                Column {
                    Text(
                        text = "Confirm Password",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                    )
                    BasicTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            isConfirmPasswordValid = it == password && password.length >= 6 // Validate against password
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isConfirmPasswordValid) Color.LightGray else Color.Red.copy(alpha = 0.3f))
                            .padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (confirmPassword.isEmpty()) {
                                Text(
                                    text = "Confirm password",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (!isConfirmPasswordValid) {
                        Text(text = "Passwords do not match or are too short", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field with Label
                Column {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                    )
                    BasicTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isEmailValid) Color.LightGray else Color.Red.copy(alpha = 0.3f))
                            .padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (email.isEmpty()) {
                                Text(
                                    text = "Enter email",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (!isEmailValid) {
                        Text(text = "Invalid email address", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isUsernameValid && isPasswordValid && isConfirmPasswordValid && isEmailValid) {
                            scope.launch {
                                val (usernameExists, emailExists) = databaseManager.userExists(username, email)
                                if (usernameExists) {
                                    registerMessage = "Username already exists"
                                } else if (emailExists) {
                                    registerMessage = "Email already exists"
                                } else {
                                    val success = databaseManager.registerUser(username, email, password)
                                    registerMessage = if (success) "Registration successful" else "Registration failed"
                                }
                            }
                        } else {
                            registerMessage = "Please fill in all fields correctly"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Register", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = registerMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (registerMessage.startsWith("Registration")) Color.Green else Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    BaseConverterTheme {
        RegisterActivity().RegisterScreen()
    }
}
