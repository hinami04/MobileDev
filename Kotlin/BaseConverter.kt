package com.example.baseconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.NumberFormatException


class BaseConvertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                BaseConverterScreen(onBackClick = { finish() })

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverterScreen(onBackClick: () -> Unit) {
    var inputNumber by remember { mutableStateOf("") }
    var selectedConversion by remember { mutableStateOf("Decimal to Octal") }
    var result by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Conversion options
    val conversions = listOf(
        "Decimal to Octal",
        "Decimal to Hexadecimal",
        "Octal to Decimal",
        "Octal to Hexadecimal",
        "Hexadecimal to Decimal",
        "Hexadecimal to Octal"
    )

    // Function to perform conversion based on selected type
    fun convertNumber() {
        try {
            if (inputNumber.isBlank()) {
                result = ""
                errorMessage = "Please enter a number"
                return
            }

            val resultValue = when (selectedConversion) {
                "Decimal to Octal" -> inputNumber.toLong(10).toString(8)
                "Decimal to Hexadecimal" -> inputNumber.toLong(10).toString(16)
                "Octal to Decimal" -> inputNumber.toLong(8).toString(10)
                "Octal to Hexadecimal" -> {
                    val decimal = inputNumber.toLong(8)
                    decimal.toString(16)
                }
                "Hexadecimal to Decimal" -> inputNumber.toLong(16).toString(10)
                "Hexadecimal to Octal" -> {
                    val decimal = inputNumber.toLong(16)
                    decimal.toString(8)
                }
                else -> ""
            }
            result = resultValue.uppercase()
            errorMessage = ""
        } catch (e: NumberFormatException) {
            val base = when (selectedConversion) {
                "Decimal to Octal", "Decimal to Hexadecimal" -> "Decimal"
                "Octal to Decimal", "Octal to Hexadecimal" -> "Octal"
                "Hexadecimal to Decimal", "Hexadecimal to Octal" -> "Hexadecimal"
                else -> ""
            }
            errorMessage = "Invalid $base number"
            result = ""
        } catch (e: Exception) {
            errorMessage = "Conversion error"
            result = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Base Converter", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFEE6983))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFFF5E4).copy(alpha = 0.4f))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input and Conversion Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputNumber,
                        onValueChange = {
                            inputNumber = it
                            convertNumber()
                        },
                        label = {
                            Text("Enter ${selectedConversion.split(" ")[0]} Number")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFFC4C4),
                            cursorColor = Color(0xFFFFC4C4)
                        )
                    )

                    ConversionSelector(
                        selectedConversion = selectedConversion,
                        conversions = conversions,
                        onConversionChange = {
                            selectedConversion = it
                            convertNumber()
                        }
                    )
                }
            }

            // Result Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC4C4).copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${selectedConversion.split(" to ")[1]} Result",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF850E35)
                    )
                    Text(
                        text = result,
                        fontSize = 32.sp,
                        color = Color(0xFF850E35),
                        modifier = Modifier.padding(top = 8.dp),
                        maxLines = 1
                    )
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = LightSalmon,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionSelector(
    selectedConversion: String,
    conversions: List<String>,
    onConversionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedConversion,
            onValueChange = {},
            label = { Text("Conversion Type") },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0XFFFFC4C4),
                cursorColor = Color(0xFF850E35)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            conversions.forEach { conversion ->
                DropdownMenuItem(
                    text = { Text(conversion) },
                    onClick = {
                        onConversionChange(conversion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseConverterPreview() {

        BaseConverterScreen(onBackClick = {})

}
