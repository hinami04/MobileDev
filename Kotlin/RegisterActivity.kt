package com.example.baseconverter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme
import kotlinx.coroutines.launch


class RegisterActivity : ComponentActivity() {
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
                    RegisterScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun RegisterScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var selectedRole by remember { mutableStateOf("Student") }
        var registerMessage by remember { mutableStateOf("") }
        var isUsernameValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isConfirmPasswordValid by remember { mutableStateOf(true) }
        var isEmailValid by remember { mutableStateOf(true) }
        var isRoleValid by remember { mutableStateOf(true) }

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

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
                    .padding(16.dp)
                    .verticalScroll(scrollState),
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
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Pink // Updated to Pink
                    )

                    Text(
                        text = "Please fill in your details",
                        color = DarkGray, // Updated to DarkGray
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Username field
                    Column {
                        Text(
                            text = "Username",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
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
                            placeholder = { Text("Choose a username", color = DarkGray) }, // Updated placeholder color
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

                    // Email field
                    Column {
                        Text(
                            text = "Email",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                isEmailValid = email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = { Text("Enter your email address", color = DarkGray) }, // Updated placeholder color
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White,
                                focusedBorderColor = Pink, // Updated to Pink
                                unfocusedBorderColor = DarkGray // Updated to DarkGray
                            ),
                            isError = !isEmailValid,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(color = DarkGray) // Updated text color
                        )
                        if (!isEmailValid) {
                            Text(
                                text = "Invalid email address",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // Password field
                    Column {
                        Text(
                            text = "Password",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                isPasswordValid = password.length >= 6
                                isConfirmPasswordValid = confirmPassword.isEmpty() || confirmPassword == password
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = { Text("Create a password (min 6 characters)", color = DarkGray) }, // Updated placeholder color
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

                    // Confirm Password field
                    Column {
                        Text(
                            text = "Confirm Password",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                isConfirmPasswordValid = it == password
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = { Text("Re-enter your password", color = DarkGray) }, // Updated placeholder color
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White,
                                focusedBorderColor = Pink, // Updated to Pink
                                unfocusedBorderColor = DarkGray // Updated to DarkGray
                            ),
                            isError = !isConfirmPasswordValid,
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
                        if (!isConfirmPasswordValid) {
                            Text(
                                text = "Passwords do not match",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // Role Selection Dropdown
                    Column {
                        Text(
                            text = "Register as",
                            color = DarkGray, // Updated to DarkGray
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
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

                    // Register button
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            validateAndRegister(
                                username, email, password, confirmPassword, selectedRole,
                                isUsernameValid, isEmailValid, isPasswordValid, isConfirmPasswordValid, isRoleValid,
                                scope, databaseManager
                            ) { message -> registerMessage = message }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Updated to Maroon
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 2.dp)
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White // Updated to white for contrast
                        )
                    }

                    // Register message
                    if (registerMessage.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (registerMessage.startsWith("Registration successful"))
                                    Pink.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f) // Updated success background to Pink
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = registerMessage,
                                textAlign = TextAlign.Center,
                                color = if (registerMessage.startsWith("Registration successful"))
                                    Pink else Color.Red, // Updated success text to Pink
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }

                    // Back to login button
                    TextButton(
                        onClick = { finish() },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Already have an account? Sign In",
                            color = Pink, // Updated to Pink
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
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

    private fun validateAndRegister(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String,
        isUsernameValid: Boolean,
        isEmailValid: Boolean,
        isPasswordValid: Boolean,
        isConfirmPasswordValid: Boolean,
        isRoleValid: Boolean,
        scope: kotlinx.coroutines.CoroutineScope,
        databaseManager: DatabaseManager,
        setMessage: (String) -> Unit
    ) {
        if (!isUsernameValid) {
            setMessage("Username cannot be empty")
            return
        }
        if (!isEmailValid) {
            setMessage("Please enter a valid email address")
            return
        }
        if (!isPasswordValid) {
            setMessage("Password must be at least 6 characters")
            return
        }
        if (!isConfirmPasswordValid) {
            setMessage("Passwords do not match")
            return
        }
        if (!isRoleValid) {
            setMessage("Please select a role")
            return
        }

        scope.launch {
            Log.d("Register", "Checking user: $username, Email: $email")
            val (usernameExists, emailExists) = databaseManager.userExists(username, email)
            when {
                usernameExists -> setMessage("Username already exists")
                emailExists -> setMessage("Email already exists")
                else -> {
                    Log.d("Register", "Registering: $username, Role: $role")
                    val (success, errorMessage) = databaseManager.registerUserWithRole(username, email, password, role)
                    if (success) {
                        Log.d("Register", "Registration successful for $username")
                        setMessage("Registration successful! You can now sign in.")
                    } else {
                        Log.e("Register", "Registration failed for $username: $errorMessage")
                        setMessage("Registration failed: $errorMessage")
                    }
                }
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
