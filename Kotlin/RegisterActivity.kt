package com.example.baseconvert

import android.os.Bundle
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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

import kotlinx.coroutines.launch

//LightYellow = Color(0xFFFFF5E4)
//val Maroon = Color(0xFF660000)
//val LightRed = Color(0xFFFFA8A8)

class RegisterActivity : ComponentActivity() {
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager()

        setContent {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF98FB98)
                ) {
                    RegisterScreen()
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
        var registerMessage by remember { mutableStateOf("") }
        var isUsernameValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isConfirmPasswordValid by remember { mutableStateOf(true) }
        var isEmailValid by remember { mutableStateOf(true) }

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        // Background with app branding
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFFF5E4)),
            contentAlignment = Alignment.Center
        ) {
            // Registration card
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFA8A8)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Registration header
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "Please fill in your details",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Username field
                    Column {
                        Text(
                            text = "Username",
                            color = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 18.sp
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
                            placeholder = { Text("Enter username") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color(0xFFFFA07A),
                                focusedBorderColor = Color(0xFF2E8B57),
                                unfocusedBorderColor = Color(0xFF90EE90)
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
                            singleLine = true
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
                            color = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 18.sp
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
                            placeholder = { Text("Enter email address") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF90EE90),
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color(0xFFFFA07A),
                                focusedBorderColor = Color(0xFF2E8B57),
                                unfocusedBorderColor = Color(0xFF90EE90)
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
                            singleLine = true
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
                            color = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 18.sp
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
                            placeholder = { Text("Create a password") },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF90EE90),
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color(0xFFFFA07A),
                                focusedBorderColor = Color(0xFF2E8B57),
                                unfocusedBorderColor = Color(0xFF90EE90)
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
                            singleLine = true
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
                            color = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 4.dp),
                            fontSize = 18.sp
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
                            placeholder = { Text("Re-enter your password") },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF90EE90),
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color(0xFFFFA07A),
                                focusedBorderColor = Color(0xFF2E8B57),
                                unfocusedBorderColor = Color(0xFF90EE90)
                            ),
                            isError = !isConfirmPasswordValid,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    validateAndRegister(
                                        username, email, password, confirmPassword,
                                        isUsernameValid, isEmailValid, isPasswordValid, isConfirmPasswordValid,
                                        scope, databaseManager
                                    ) { message -> registerMessage = message }
                                }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Register button with improved styling
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            validateAndRegister(
                                username, email, password, confirmPassword,
                                isUsernameValid, isEmailValid, isPasswordValid, isConfirmPasswordValid,
                                scope, databaseManager
                            ) { message -> registerMessage = message }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF660000)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Register message (error or success)
                    if (registerMessage.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (registerMessage.startsWith("Registration successful"))
                                    Color(0xFF006400).copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = registerMessage,
                                textAlign = TextAlign.Center,
                                color = if (registerMessage.startsWith("Registration successful"))
                                    Color(0xFF006400) else Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }

                    // Back to login button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Sign In",
                            color = Color(0xFF660000),
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { finish() } //terminate activity, back to log in page
                        )
                    }
                }
            }

            // Footer text
            Text(
                text = "© 2025 Base Converter",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                fontSize = 12.sp,
                color = Color(0xFF850E35)
            )
        }
    }

    // Helper function to validate and handle registration
    private fun validateAndRegister(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        isUsernameValid: Boolean,
        isEmailValid: Boolean,
        isPasswordValid: Boolean,
        isConfirmPasswordValid: Boolean,
        scope: kotlinx.coroutines.CoroutineScope,
        databaseManager: DatabaseManager,
        setMessage: (String) -> Unit
    ) {
        if (username.isEmpty()) {
            setMessage("Username cannot be empty")
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setMessage("Please enter a valid email address")
            return
        }

        if (password.length < 6) {
            setMessage("Password must be at least 6 characters")
            return
        }

        if (password != confirmPassword) {
            setMessage("Passwords do not match")
            return
        }

        if (isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            scope.launch {
                val (usernameExists, emailExists) = databaseManager.userExists(username, email)
                when {
                    usernameExists -> setMessage("Username already exists")
                    emailExists -> setMessage("Email already exists")
                    else -> {
                        val success = databaseManager.registerUser(username, email, password)
                        setMessage(if (success) "Registration successful! You can now sign in." else "Registration failed. Please try again.")
                    }
                }
            }
        } else {
            setMessage("Please fill in all fields correctly")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {

        RegisterActivity().RegisterScreen()

}
