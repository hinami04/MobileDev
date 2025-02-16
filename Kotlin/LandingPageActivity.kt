package com.example.cudera

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cudera.ui.theme.CuderaTheme

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuderaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandingPage { navigateToProfileScreen() }
                }
            }
        }
    }

    private fun navigateToProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LandingPage(onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the Landing Page", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onProfileClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    CuderaTheme {
        LandingPage {}
    }
}
