package com.kashef.customdatepicker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.customdatepicker.DatePickerBottomSheet
import com.kashef.customdatepicker.ui.theme.CustomDatePickerTheme
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomDatePickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DatePickerExamplesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerExamplesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Custom Date Picker Examples",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Example 1: Basic Single Date Selection
        BasicSingleDateExample()

        // Example 2: Date Range Selection
        DateRangeSelectionExample()

        // Example 3: Limited Available Dates
        LimitedAvailableDatesExample()

        // Example 4: Pre-selected Date
        PreSelectedDateExample()

        // Example 5: Custom Locale (Arabic)
        CustomLocaleExample()

        // Example 6: Weekends Only
        WeekendsOnlyExample()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BasicSingleDateExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("No date selected") }

    ExampleCard(
        title = "1. Basic Single Date Selection",
        description = "Simple single date picker with all dates available"
    ) {
        Text("Selected: $selectedDate")

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Date")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateAvailableDates(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                isMultiSelect = false,
                saveButtonText = "Select Date"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangeSelectionExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("No range selected") }

    ExampleCard(
        title = "2. Date Range Selection",
        description = "Select start and end dates for a range"
    ) {
        Text("Selected Range: $selectedDateRange")

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Date Range")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateAvailableDates(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { dateRange ->
                    selectedDateRange = if (dateRange.contains(",")) {
                        val dates = dateRange.split(",")
                        "From ${dates[0]} to ${dates[1]}"
                    } else {
                        "Start: $dateRange"
                    }
                    showDatePicker = false
                },
                isMultiSelect = true,
                saveButtonText = "Select Range"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LimitedAvailableDatesExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("No date selected") }

    ExampleCard(
        title = "3. Limited Available Dates",
        description = "Only specific dates are available for selection"
    ) {
        Text("Selected: $selectedDate")
        Text(
            "Available: Only weekdays in current month",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Weekday")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateWeekdaysOnly(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                isMultiSelect = false,
                saveButtonText = "Select Weekday"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreSelectedDateExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }

    ExampleCard(
        title = "4. Pre-selected Date",
        description = "Date picker opens with a pre-selected date"
    ) {
        Text("Selected: $selectedDate")

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Date")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateAvailableDates(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                initialDate = selectedDate,
                isMultiSelect = false,
                saveButtonText = "Update Date"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomLocaleExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("No date selected") }

    ExampleCard(
        title = "5. Custom Locale (Arabic)",
        description = "Date picker with Arabic locale"
    ) {
        Text("Selected: $selectedDate")

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Date (Arabic)")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateAvailableDates(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                isMultiSelect = false,
                saveButtonText = "حفظ التاريخ",
                locale = Locale("ar")
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekendsOnlyExample() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("No weekend selected") }

    ExampleCard(
        title = "6. Weekends Only Range",
        description = "Only weekends are available for range selection"
    ) {
        Text("Selected: $selectedDateRange")

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Weekend Range")
        }

        if (showDatePicker) {
            DatePickerBottomSheet(
                availableDates = generateWeekendsOnly(),
                onDismiss = { showDatePicker = false },
                onSaveDate = { dateRange ->
                    selectedDateRange = if (dateRange.contains(",")) {
                        val dates = dateRange.split(",")
                        "Weekend: ${dates[0]} to ${dates[1]}"
                    } else {
                        "Start: $dateRange"
                    }
                    showDatePicker = false
                },
                isMultiSelect = true,
                saveButtonText = "Select Weekends"
            )
        }
    }
}

@Composable
fun ExampleCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

// Helper functions to generate different types of available dates
@RequiresApi(Build.VERSION_CODES.O)
fun generateAvailableDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (0..60).map { today.plusDays(it.toLong()) }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateWeekdaysOnly(): List<LocalDate> {
    val today = LocalDate.now()
    return (0..30).map { today.plusDays(it.toLong()) }
        .filter { date ->
            val dayOfWeek = date.dayOfWeek.value
            dayOfWeek in 1..5 // Monday to Friday
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateWeekendsOnly(): List<LocalDate> {
    val today = LocalDate.now()
    return (0..60).map { today.plusDays(it.toLong()) }
        .filter { date ->
            val dayOfWeek = date.dayOfWeek.value
            dayOfWeek == 6 || dayOfWeek == 7 // Saturday and Sunday
        }
}