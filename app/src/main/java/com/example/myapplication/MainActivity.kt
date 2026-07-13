package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BusinessCard(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BusinessCard(modifier: Modifier = Modifier) {
    // Surface handles the background and ensures content colors adapt correctly
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular Avatar - Centered by the Column's horizontalAlignment
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF771C1B), CircleShape),
                contentScale = ContentScale.Crop 
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Ashton Nathaniel S. Lactuan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF771C1B)
            )
            Text(
                text = "Android Instructor",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))
            
            // Nested Column for contact info to keep icons/text aligned together
            Column {
                ContactRow(Icons.Default.Phone, "09452141843")
                ContactRow(Icons.Default.Email, "anlactuan93824@liceo.edu.ph")
            }
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF771C1B)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(name = "Card - Light", showBackground = true, widthDp = 360)
@Composable
fun BusinessCardPreview() {
    // Disable dynamicColor to ensure the preview uses your defined theme colors
    MyApplicationTheme(dynamicColor = false) {
        BusinessCard()
    }
}

@Preview(
    name = "Card - Dark",
    showBackground = true,
    widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BusinessCardDarkPreview() {
    // Disable dynamicColor to ensure the preview shows the Dark Mode properly
    MyApplicationTheme(dynamicColor = false) {
        BusinessCard()
    }
}

@Preview(
    name = "Card - Light Big Font",
    showBackground = true,
    widthDp = 360,
    fontScale = 1.5f
)
@Composable
fun BusinessCardBigPreview() {
    MyApplicationTheme(dynamicColor = false) {
        BusinessCard()
    }
}
