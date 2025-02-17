package com.example.baseconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baseconverter.ui.theme.BaseConverterTheme

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandingPage(
                        onProfileClick = { navigateToProfileScreen() },
                        onSettingsClick = { navigateToSettingsScreen() }
                    )
                }
            }
        }
    }

    private fun navigateToProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(onProfileClick: () -> Unit, onSettingsClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "BaseConvert", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            BottomNavigationBar(onSettingsClick = onSettingsClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to BaseConvert",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Your one-stop solution for all conversion needs",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Go to Profile", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
            }

            FeaturesSection()
            TestimonialsSection()
            ContactUsSection()
        }
    }
}

@Composable
fun FeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Features", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "• Easy conversion between various units", style = MaterialTheme.typography.bodyLarge)
        Text(text = "• User-friendly interface", style = MaterialTheme.typography.bodyLarge)
        Text(text = "• Real-time conversion results", style = MaterialTheme.typography.bodyLarge)
        Text(text = "• Customizable settings", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TestimonialsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Testimonials", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "\"BaseConvert is the best conversion app I've ever used!\"", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(text = "- User A", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "\"The user interface is so intuitive and easy to use. Highly recommended!\"", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(text = "- User B", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
fun ContactUsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Contact Us", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Email: support@baseconvert.com", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Phone: +1 123 456 7890", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Address: 123 BaseConvert Street, City, Country", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun BottomNavigationBar(onSettingsClick: () -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    BaseConverterTheme {
        LandingPage({}, {})
    }
}
