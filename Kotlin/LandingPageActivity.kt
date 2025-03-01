package com.example.baseconverter

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.baseconverter.ui.theme.BaseConverterTheme
import java.util.Calendar

val MintGreen = Color(0xFF98FB98)
val LightMint = Color(0xFF90EE90)
val MediumSeaGreen = Color(0xFF3CB371)
val DarkSeaGreen = Color(0xFF2E8B57)
val LightSalmon = Color(0xFFFFA07A)

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightMint.copy(alpha = 0.1f)
                ) {
                    val username = intent.getStringExtra("logged_in_user") ?: "User"
                    LandingPage(
                        username = username,
                        onProfileClick = { navigateToProfileScreen(username) },
                        onBaseConvertClick = { navigateToBaseConverter() },
                        onLogoutClick = { navigateToLoginScreen() }
                    )
                }
            }
        }
    }

    private fun navigateToProfileScreen(username: String) {
        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra("USERNAME", username)
        }
        startActivity(intent)
    }

    private fun navigateToBaseConverter() {
        val intent = Intent(this, BaseConverterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(
    username: String,
    onProfileClick: () -> Unit,
    onBaseConvertClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences("ConversionHistory", Context.MODE_PRIVATE)
    val history by remember { mutableStateOf(prefs.getStringSet("history", emptySet())?.toList() ?: emptyList()) }
    var showOptionsDialog by remember { mutableStateOf(false) }

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMint.copy(alpha = 0.1f))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumSeaGreen)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$greeting, $username",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            // The logout button has been removed from here
        }

        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BaseConvert",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkSeaGreen,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Simplify your number conversions",
                            fontSize = 18.sp,
                            color = MediumSeaGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        title = "Profile",
                        subtitle = "Your stats",
                        color = DarkSeaGreen.copy(alpha = 0.25f),
                        onClick = onProfileClick,
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Convert",
                        subtitle = "Start converting",
                        color = DarkSeaGreen.copy(alpha = 0.25f),
                        onClick = onBaseConvertClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Conversion
            item {
                QuickConverterCard(onBaseConvertClick = onBaseConvertClick)
            }

            // Recent Conversions
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Recent Conversions",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (history.isEmpty()) {
                            Text(
                                text = "No recent conversions",
                                color = MediumSeaGreen,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } else {
                            history.take(3).forEach { entry ->
                                Text(
                                    text = entry,
                                    color = MediumSeaGreen,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Features Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Features",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSeaGreen,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        FeatureItem("Instant Conversions", "Real-time base conversion")
                        FeatureItem("Multiple Bases", "Binary, Octal, Decimal, Hex")
                        FeatureItem("History Tracking", "Save your conversions")
                    }
                }
            }
        }

        // Floating Action Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { showOptionsDialog = true },
                modifier = Modifier
                    .size(60.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showOptionsDialog) {
        OptionsDialog(
            onDismiss = { showOptionsDialog = false },
            onBaseConvertClick = onBaseConvertClick,
            onProfileClick = onProfileClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickConverterCard(onBaseConvertClick: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Quick Convert (Dec to Hex)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSeaGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = input,
                onValueChange = {
                    input = it
                    result = try {
                        if (it.isBlank()) "" else it.toLong(10).toString(16).uppercase()
                    } catch (e: Exception) {
                        "Error"
                    }
                },
                label = { Text("Enter Decimal", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MediumSeaGreen,
                    unfocusedBorderColor = DarkSeaGreen,
                    cursorColor = MediumSeaGreen,
                    focusedLabelColor = MediumSeaGreen,
                    unfocusedLabelColor = DarkSeaGreen
                )
            )
            Text(
                text = "Hex Result: $result",
                fontSize = 18.sp,
                color = MediumSeaGreen,
                modifier = Modifier.padding(top = 12.dp)
            )
            Button(
                onClick = onBaseConvertClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Full Converter", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun OptionsDialog(
    onDismiss: () -> Unit,
    onBaseConvertClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .padding(16.dp)
                .width(280.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Options",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSeaGreen
                )
                Button(
                    onClick = onBaseConvertClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Base Converter", color = Color.White, fontSize = 18.sp)
                }
                Button(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Profile", color = Color.White, fontSize = 18.sp)
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightSalmon),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSeaGreen
            )
            Text(
                text = subtitle,
                fontSize = 16.sp,
                color = MediumSeaGreen,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun FeatureItem(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(MediumSeaGreen, CircleShape)
                .shadow(1.dp, CircleShape)
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkSeaGreen
            )
            Text(
                text = description,
                fontSize = 16.sp,
                color = MediumSeaGreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    BaseConverterTheme {
        LandingPage(username = "User", {}, {}, {})
    }
}
