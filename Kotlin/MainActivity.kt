package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme
import kotlinx.coroutines.launch

// Color schem

class MainActivity : ComponentActivity() {
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
                    LoginScreen(
                        onRegisterClick = { navigateToRegisterScreen() },
                        onLoginSuccess = { username, role ->
                            navigateToDashboard(username, role)
                        }
                    )
                }
            }
        }
    }

    private fun navigateToRegisterScreen() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDashboard(username: String, role: String) {
        val intent = when (role) {
            "Tutor" -> Intent(this, TutorDashboardActivity::class.java)
            "Student" -> Intent(this, StudentDashboardActivity::class.java)
            else -> Intent(this, MainActivity::class.java) // Fallback
        }.apply {
            putExtra("logged_in_user", username)
            putExtra("user_role", role)
        }
        startActivity(intent)
        finish()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun LoginScreen(onRegisterClick: () -> Unit, onLoginSuccess: (String, String) -> Unit) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var selectedRole by remember { mutableStateOf("Student") }
        var loginMessage by remember { mutableStateOf("") }
        var isUsernameValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isRoleValid by remember { mutableStateOf(true) }

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPink), // Updated to LightPink
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Base Converter",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Pink // Updated to Pink
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightYellow), // Updated to LightYellow
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Pink // Updated to Pink
                    )

                    Text(
                        text = "Please sign in to continue",
                        color = DarkGray, // Updated to DarkGray
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Username Field
                    Column {
                        Text(
                            text = "Username",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier.padding(bottom = 4.dp),
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                isUsernameValid = username.isNotEmpty()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White,
                                focusedBorderColor = Pink, // Updated to Pink
                                unfocusedBorderColor = DarkGray // Updated to DarkGray
                            ),
                            isError = !isUsernameValid,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(color = DarkGray) // Updated text color
                        )
                        if (!isUsernameValid) {
                            Text(
                                text = "Username cannot be empty",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // Password Field
                    Column {
                        Text(
                            text = "Password",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier.padding(bottom = 4.dp),
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                isPasswordValid = password.length >= 6
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White,
                                focusedBorderColor = Pink, // Updated to Pink
                                unfocusedBorderColor = DarkGray // Updated to DarkGray
                            ),
                            isError = !isPasswordValid,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(color = DarkGray) // Updated text color
                        )
                        if (!isPasswordValid) {
                            Text(
                                text = "Password must be at least 6 characters",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // Role Selection Dropdown
                    Column {
                        Text(
                            text = "Login as",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier.padding(bottom = 4.dp),
                            fontSize = 14.sp
                        )
                        RoleDropdown(
                            selectedRole = selectedRole,
                            onRoleSelected = { role ->
                                selectedRole = role
                                isRoleValid = role.isNotEmpty()
                            }
                        )
                        if (!isRoleValid) {
                            Text(
                                text = "Please select a role",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sign In Button
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            if (isUsernameValid && isPasswordValid && isRoleValid) {
                                scope.launch {
                                    Log.d("Login", "Attempting login: $username, Role: $selectedRole")
                                    val (success, dbRole) = databaseManager.loginUserWithRole(username, password, selectedRole)
                                    if (success) {
                                        Log.d("Login", "Login successful for $username as $dbRole")
                                        onLoginSuccess(username, selectedRole)
                                    } else {
                                        Log.e("Login", "Login failed for $username")
                                        loginMessage = "Invalid login credentials or role mismatch"
                                    }
                                }
                            } else {
                                loginMessage = "Please fill in all fields correctly"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 2.dp)
                    ) {
                        Text(
                            text = "SIGN IN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White // Updated to white for contrast
                        )
                    }

                    if (loginMessage.isNotEmpty()) {
                        Text(
                            text = loginMessage,
                            textAlign = TextAlign.Center,
                            color = if (loginMessage.startsWith("Invalid")) Color.Red else Pink, // Updated success message to Pink
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            color = DarkGray, // Updated to DarkGray
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Register",
                            color = Pink, // Updated to Pink
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onRegisterClick() }
                        )
                    }
                }
            }

            Text(
                text = "© 2025 Base Converter",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                fontSize = 12.sp,
                color = DarkGray // Updated to DarkGray
            )
        }
    }

    @Composable
    fun RoleDropdown(selectedRole: String, onRoleSelected: (String) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val roles = listOf("Student", "Tutor")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White), // Updated to white for consistency with text fields
        ) {
            Text(
                text = selectedRole,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(16.dp),
                color = DarkGray // Updated to DarkGray
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Updated to white
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(text = role, color = DarkGray) }, // Updated to DarkGray
                        onClick = {
                            onRoleSelected(role)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BaseConverterTheme {
        MainActivity().LoginScreen({}, { _, _ -> })
    }
}
