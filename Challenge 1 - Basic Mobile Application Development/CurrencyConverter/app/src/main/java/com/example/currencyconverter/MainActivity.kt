package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import java.util.Locale
import androidx.compose.ui.unit.dp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// API_KEY
// Get your API key from https://exchangeratesapi.io/
const val API_KEY = ""


// Data classes for API response
data class GetSymbolsResponse(
    val success: Boolean, val symbols: Map<String, String>
)

data class GetLatestResponse(
    val success: Boolean,
    val timestamp: Int,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

// Retrofit Interface
interface ExchangeRatesApi {
    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") accessKey: String
    ): GetSymbolsResponse

    @GET("latest")
    suspend fun getLatest(
        @Query("access_key") accessKey: String,
    ): GetLatestResponse
}

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val retrofit = Retrofit.Builder().baseUrl("https://api.exchangeratesapi.io/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(ExchangeRatesApi::class.java)
        setContent {
            CurrencyConverterTheme {
                // Replace boilerplate code with CurrencyConverterScreen
                CurrencyConverterScreen(api)
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen(api: ExchangeRatesApi) {
    var isLoading by remember { mutableStateOf(true) }
    var symbols by remember { mutableStateOf<Map<String, String>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var amount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("VND") }
    var fromCurrencyExpanded by remember { mutableStateOf(false) }
    var toCurrencyExpanded by remember { mutableStateOf(false) }
    val rates = remember { mutableStateOf<Map<String, Double>?>(null) }

    // Call API when the screen is launched
    LaunchedEffect(Unit) {
        // Fetch symbols from API
        try {
            val response = api.getSymbols(API_KEY)
            if (response.success) {
                // Assign available symbols to symbols variable
                symbols = response.symbols
            } else {
                error = "API call failed."
            }
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }

        // Fetch rates from API
        try {
            val response = api.getLatest(API_KEY)
            if (response.success) {
                // Assign rates to rates variable
                rates.value = response.rates
            } else {
                error = "API call failed."
            }
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }

        isLoading = false
    }

    // Display loading UI
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        // Display error message
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = error ?: "Unknown Error")
        }
    } else if (symbols != null) {
        // Display Currency Converter UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Currency Converter",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            // From heading
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text("From:", style = MaterialTheme.typography.headlineSmall)
            }
            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Amount TextField
                TextField(value = amount,
                    onValueChange = {
                        // when text changes, calculate the result immediately base on the rates fetched from API
                        amount = it
                        result = if (it.isNotEmpty()) String.format(
                            Locale.US,
                            "%.3f",
                            amount.toDouble() * rates.value!![toCurrency]!! / rates.value!![fromCurrency]!!
                        ) else ""

                    },
                    label = { Text(text = "Amount", style = MaterialTheme.typography.bodyLarge) },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Dropdown for selecting from currency
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { fromCurrencyExpanded = true })
                ) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                            .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fromCurrency, style = MaterialTheme.typography.headlineSmall
                        )
                        IconButton(onClick = { fromCurrencyExpanded = !fromCurrencyExpanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                        }
                    }

                    DropdownMenu(expanded = fromCurrencyExpanded,
                        onDismissRequest = { fromCurrencyExpanded = false }) {
                        symbols!!.forEach { option ->
                            DropdownMenuItem(text = { Text(option.key + " - " + option.value) },
                                onClick = {
                                    fromCurrency = option.key
                                    fromCurrencyExpanded = false
                                })
                        }
                    }
                }
            }

            // To heading
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text("To:", style = MaterialTheme.typography.headlineSmall)
            }
            // Result Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Result TextField (read-only) and be updated when the amount changes
                TextField(value = result,
                    onValueChange = {},
                    label = { Text(text = "Result", style = MaterialTheme.typography.bodyLarge) },
                    readOnly = true,
                    textStyle = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Dropdown for selecting to currency
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { toCurrencyExpanded = true })
                ) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                            .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = toCurrency, style = MaterialTheme.typography.headlineSmall
                        )
                        IconButton(onClick = { toCurrencyExpanded = !toCurrencyExpanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                        }
                    }
                    DropdownMenu(expanded = toCurrencyExpanded,
                        onDismissRequest = { toCurrencyExpanded = false }) {
                        symbols!!.forEach { option ->
                            DropdownMenuItem(text = { Text(option.key + " - " + option.value) },
                                onClick = {
                                    toCurrency = option.key
                                    toCurrencyExpanded = false
                                })
                        }
                    }
                }
            }
        }
    }
}