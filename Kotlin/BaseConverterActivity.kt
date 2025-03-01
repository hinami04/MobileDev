package com.example.baseconverter

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import com.example.baseconverter.ui.theme.BaseConverterTheme
import java.lang.NumberFormatException
import android.content.SharedPreferences
import androidx.compose.ui.text.style.TextAlign

class BaseConverterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConverterTheme {
                BaseConverterScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverterScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("ConversionHistory", Context.MODE_PRIVATE)
    var inputNumber by remember { mutableStateOf("") }
    var selectedConversion by remember { mutableStateOf("Decimal to Octal") }
    var result by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }
    val history = remember { mutableStateListOf<String>() }

    // Load history from SharedPreferences
    LaunchedEffect(Unit) {
        val savedHistory = prefs.getStringSet("history", emptySet())?.toList() ?: emptyList()
        history.addAll(savedHistory)
    }

    // Conversion options
    val conversions = listOf(
        "Decimal to Binary", "Decimal to Octal", "Decimal to Hexadecimal",
        "Binary to Decimal", "Binary to Octal", "Binary to Hexadecimal",
        "Octal to Decimal", "Octal to Binary", "Octal to Hexadecimal",
        "Hexadecimal to Decimal", "Hexadecimal to Binary", "Hexadecimal to Octal"
    )

    // Input validation for each base
    fun isValidInput(input: String, base: String): Boolean {
        return when (base) {
            "Decimal" -> input.all { it.isDigit() }
            "Binary" -> input.all { it == '0' || it == '1' }
            "Octal" -> input.all { it in '0'..'7' }
            "Hexadecimal" -> input.all { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }
            else -> false
        }
    }

    // Function to perform conversion
    fun convertNumber() {
        if (inputNumber.isBlank()) {
            result = ""
            errorMessage = "Please enter a number"
            return
        }

        val (fromBaseStr, toBaseStr) = selectedConversion.split(" to ")
        if (!isValidInput(inputNumber, fromBaseStr)) {
            errorMessage = "Invalid $fromBaseStr number"
            result = ""
            return
        }

        try {
            // Step 1: Convert input to decimal (Long)
            val decimalValue = when (fromBaseStr) {
                "Decimal" -> inputNumber.toLong(10)
                "Binary" -> inputNumber.toLong(2)
                "Octal" -> inputNumber.toLong(8)
                "Hexadecimal" -> inputNumber.toLong(16)
                else -> throw IllegalArgumentException("Unknown base: $fromBaseStr")
            }

            // Step 2: Convert decimal to target base
            result = when (toBaseStr) {
                "Decimal" -> decimalValue.toString(10)
                "Binary" -> decimalValue.toString(2)
                "Octal" -> decimalValue.toString(8)
                "Hexadecimal" -> decimalValue.toString(16).uppercase() // Ensure Hex is uppercase
                else -> throw IllegalArgumentException("Unknown base: $toBaseStr")
            }

            errorMessage = ""
            // Add to history
            val conversionEntry = "$inputNumber ($fromBaseStr) → $result ($toBaseStr)"
            if (!history.contains(conversionEntry)) {
                history.add(0, conversionEntry)
                if (history.size > 10) history.removeAt(history.size - 1)
                prefs.edit().putStringSet("history", history.toSet()).apply()
            }
        } catch (e: NumberFormatException) {
            errorMessage = "Invalid $fromBaseStr number"
            result = ""
        } catch (e: Exception) {
            errorMessage = "Conversion error: ${e.message}"
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
                actions = {
                    TextButton(onClick = { showHistory = true }) {
                        Text("History", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MediumSeaGreen)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightMint.copy(alpha = 0.1f))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input and Conversion Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputNumber,
                        onValueChange = {
                            inputNumber = it
                            convertNumber()
                        },
                        label = { Text("Enter ${selectedConversion.split(" ")[0]} Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MediumSeaGreen,
                            unfocusedBorderColor = DarkSeaGreen,
                            cursorColor = MediumSeaGreen
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = when (selectedConversion.split(" ")[0]) {
                                "Hexadecimal" -> KeyboardType.Text
                                else -> KeyboardType.Number
                            }
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { inputNumber = ""; result = ""; errorMessage = "" },
                            colors = ButtonDefaults.buttonColors(containerColor = LightSalmon),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Clear", color = Color.White)
                        }
                        Button(
                            onClick = {
                                val clipboard = getSystemService(context, ClipboardManager::class.java)
                                clipboard?.setPrimaryClip(android.content.ClipData.newPlainText("Result", result))
                                Toast.makeText(context, "Result copied!", Toast.LENGTH_SHORT).show()
                            },
                            enabled = result.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = MediumSeaGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Copy Result", color = Color.White)
                        }
                    }
                }
            }

            // Result Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${selectedConversion.split(" to ")[1]} Result",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSeaGreen
                    )
                    Text(
                        text = result.ifEmpty { "—" },
                        fontSize = 32.sp,
                        color = MediumSeaGreen,
                        modifier = Modifier.padding(top = 8.dp),
                        maxLines = 1
                    )
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = LightSalmon,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // History Dialog
        if (showHistory) {
            AlertDialog(
                onDismissRequest = { showHistory = false },
                title = { Text("Conversion History", color = DarkSeaGreen) },
                text = {
                    if (history.isEmpty()) {
                        Text("No conversions yet", color = MediumSeaGreen)
                    } else {
                        LazyColumn {
                            items(history) { entry ->
                                Text(
                                    text = entry,
                                    color = MediumSeaGreen,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showHistory = false }) {
                        Text("Close", color = MediumSeaGreen)
                    }
                },
                containerColor = Color.White
            )
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
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MediumSeaGreen,
                unfocusedBorderColor = DarkSeaGreen,
                cursorColor = MediumSeaGreen
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
    BaseConverterTheme {
        BaseConverterScreen(onBackClick = {})
    }
}
