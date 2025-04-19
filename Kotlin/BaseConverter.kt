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
import com.example.baseconvert.ui.theme.BaseConvertTheme
import java.lang.NumberFormatException

class BaseConvertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseConvertTheme(darkTheme = true) { // Consistent theme setup
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseConverterScreen(onBackClick = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverterScreen(onBackClick: () -> Unit) {
    var inputNumber by remember { mutableStateOf("") }
    var fromBase by remember { mutableStateOf("Decimal") }
    var toBase by remember { mutableStateOf("Octal") }
    var result by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val bases = listOf("Decimal", "Octal", "Hexadecimal")

    fun convertNumber() {
        try {
            if (inputNumber.isBlank()) {
                result = ""
                errorMessage = "Please enter a number"
                return
            }

            val decimalValue = when (fromBase) {
                "Decimal" -> inputNumber.toLong(10)
                "Octal" -> inputNumber.toLong(8)
                "Hexadecimal" -> inputNumber.toLong(16)
                else -> throw IllegalArgumentException("Invalid fromBase")
            }

            result = when (toBase) {
                "Decimal" -> decimalValue.toString(10)
                "Octal" -> decimalValue.toString(8)
                "Hexadecimal" -> decimalValue.toString(16)
                else -> throw IllegalArgumentException("Invalid toBase")
            }.uppercase()

            errorMessage = ""
        } catch (e: NumberFormatException) {
            errorMessage = "Invalid number for the selected base"
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
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF660000))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF5E4))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        label = { Text("Enter Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFFC4C4),
                            cursorColor = Color(0xFFFFC4C4)
                        )
                    )

                    BaseSelector(
                        label = "From Base",
                        selectedBase = fromBase,
                        bases = bases,
                        onBaseChange = {
                            fromBase = it
                            convertNumber()
                        }
                    )

                    BaseSelector(
                        label = "To Base",
                        selectedBase = toBase,
                        bases = bases,
                        onBaseChange = {
                            toBase = it
                            convertNumber()
                        }
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA8A8).copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Result",
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
                            color = Color.Red,
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
fun BaseSelector(
    label: String,
    selectedBase: String,
    bases: List<String>,
    onBaseChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedBase,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFFFC4C4),
                cursorColor = Color(0xFF850E35)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bases.forEach { base ->
                DropdownMenuItem(
                    text = { Text(base) },
                    onClick = {
                        onBaseChange(base)
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
    BaseConvertTheme(darkTheme = false) {
        BaseConverterScreen(onBackClick = {})
    }
}
