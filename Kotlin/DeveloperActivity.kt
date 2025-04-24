package com.example.baseconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.baseconvert.ui.theme.BaseConvertTheme

class DeveloperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConvertTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DeveloperScreen()
                }
            }
        }
    }
}

@Composable
fun DeveloperScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFFFF5E4)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF660000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Meet the Team",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TeamMember(
            profileImage = R.drawable.x1,
            name = "Cudera, Xianne\n      Jewel S.",
            bio = "A 'fake it till you make it' girly and runs by the motto 'it is what it is'.",
            funFact = "Loves sleeping and has a hidden talent for playing the guitar!"
        )

        Spacer(modifier = Modifier.height(16.dp))

        TeamMember(
            profileImage = R.drawable.wyben,
            name = "Daal, Wyben C.",
            bio = "An experienced frontend developer who thrives on solving complex problems and optimizing performance.",
            funFact = "Has a black belt in martial arts and is a coffee connoisseur."
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFF660000))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Team Vision",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "\"Innovating the future, one step at a time\"",
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Building Accessible Solutions for Everyone—empowering individuals through technology, fostering collaboration, and ensuring usability for all. BASE represents our unwavering dedication to creating tools that resonate with people from diverse backgrounds and make a meaningful difference in their everyday lives.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFF850E35))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Team Mission",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "\"Empowering innovation and collaboration to create impactful solutions for a better tomorrow.\"",
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Our team is dedicated to building innovative and user-friendly solutions that make a difference in people's lives. We believe in continuous learning, collaboration, and striving for excellence in everything we do.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun TeamMember(profileImage: Int, name: String, bio: String, funFact: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFFFA8A8).copy(alpha = 0.4f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = profileImage),
                contentDescription = "Profile Picture of $name",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Fun Fact: ")
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(funFact)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperScreenPreview() {
    BaseConvertTheme {
        DeveloperScreen()
    }
}
