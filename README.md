# Custom Date Picker for Android

A modern, customizable date picker component built with Jetpack Compose for Android applications. Features single date selection, date range selection, and extensive customization options.

## Features

✨ **Single Date Selection** - Simple date picker for selecting individual dates  
📅 **Date Range Selection** - Select start and end dates for ranges  
🎯 **Limited Available Dates** - Restrict selection to specific dates only  
🌍 **Localization Support** - Full support for different locales (including RTL languages like Arabic)  
🎨 **Custom Styling** - Customizable colors, typography, and appearance  
📱 **Bottom Sheet UI** - Modern bottom sheet presentation  
👆 **Gesture Support** - Swipe gestures for month navigation  
⚡ **Pre-selected Dates** - Initialize with pre-selected values  
🔒 **Validation** - Built-in date range validation

## Screenshots

![Screenshot_20250629_133250](https://github.com/user-attachments/assets/0f55c4b5-4d67-46ee-8c2a-09ef52785279)
![Screenshot_20250629_133327](https://github.com/user-attachments/assets/6f972c3f-98c6-4199-830d-591c1ee6bed1)
![Screenshot_20250629_133314](https://github.com/user-attachments/assets/be8a9b4b-75e0-45d7-95a1-e04b721ed534)
![Screenshot_20250629_133304](https://github.com/user-attachments/assets/63d6f78a-33e7-4313-80ef-b7e3cb14616e)

## Installation

### Step 1: Add to your project
Copy the `DatePickerBottomSheet.kt` file to your project's source directory.

### Step 2: Add dependencies
Make sure you have the following dependencies in your `build.gradle` file:

```kotlin
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.material3:material3:$material3_version"
implementation "androidx.activity:activity-compose:$activity_compose_version"
```

## Usage

### Basic Single Date Selection

```kotlin
@Composable
fun MyScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("No date selected") }

    Button(onClick = { showDatePicker = true }) {
        Text("Select Date")
    }

    if (showDatePicker) {
        DatePickerBottomSheet(
            availableDates = generateAvailableDates(), // Your list of available dates
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
```

### Date Range Selection

```kotlin
DatePickerBottomSheet(
    availableDates = availableDates,
    onDismiss = { showDatePicker = false },
    onSaveDate = { dateRange ->
        // dateRange format: "startDate,endDate" for ranges
        val dates = dateRange.split(",")
        startDate = dates[0]
        endDate = dates.getOrNull(1) ?: ""
        showDatePicker = false
    },
    isMultiSelect = true,
    saveButtonText = "Select Range"
)
```

### Custom Locale (Arabic Example)

```kotlin
DatePickerBottomSheet(
    availableDates = availableDates,
    onDismiss = { showDatePicker = false },
    onSaveDate = { date -> /* handle date */ },
    isMultiSelect = false,
    saveButtonText = "حفظ التاريخ", // Arabic text
    locale = Locale("ar")
)
```

### Pre-selected Date

```kotlin
DatePickerBottomSheet(
    availableDates = availableDates,
    initialDate = "2024-01-15", // Pre-selected date
    onDismiss = { showDatePicker = false },
    onSaveDate = { date -> /* handle date */ },
    isMultiSelect = false
)
```

## API Reference

### DatePickerBottomSheet Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `availableDates` | `List<LocalDate>` | ✅ | List of dates available for selection |
| `onDismiss` | `() -> Unit` | ✅ | Callback when bottom sheet is dismissed |
| `onSaveDate` | `(String) -> Unit` | ✅ | Callback when date(s) are saved |
| `initialDate` | `String?` | ❌ | Pre-selected date (format: YYYY-MM-DD) |
| `isMultiSelect` | `Boolean` | ❌ | Enable range selection (default: false) |
| `saveButtonText` | `String` | ❌ | Custom text for save button |
| `locale` | `Locale` | ❌ | Locale for date formatting |

### Helper Functions

```kotlin
// Generate a list of available dates
fun generateAvailableDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (0..60).map { today.plusDays(it.toLong()) }
}

// Generate only weekdays
fun generateWeekdaysOnly(): List<LocalDate> {
    return generateAvailableDates().filter { date ->
        val dayOfWeek = date.dayOfWeek.value
        dayOfWeek in 1..5 // Monday to Friday
    }
}

// Generate only weekends
fun generateWeekendsOnly(): List<LocalDate> {
    return generateAvailableDates().filter { date ->
        val dayOfWeek = date.dayOfWeek.value
        dayOfWeek == 6 || dayOfWeek == 7 // Saturday and Sunday
    }
}
```

## Customization

### Colors
Modify the `CustomDatePickerColors` object to change the color scheme:

```kotlin
object CustomDatePickerColors {
    val Primary = Color(0xFF2196F3)          // Primary color
    val PrimaryContainer = Color(0xFF2196F3)  // Selected date background
    val OnPrimary = Color.White               // Text on primary color
    val Background = Color.White              // Background color
    val OnSurface = Color.Black              // Default text color
    val Disabled = Color(0xFF9E9E9E)         // Disabled date color
}
```

### Typography
Customize text styles through the `CustomDatePickerTypography` object:

```kotlin
object CustomDatePickerTypography {
    val HeaderMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )
    // ... other text styles
}
```

## Requirements

- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 34
- **Kotlin**: 1.9.0+
- **Compose BOM**: 2024.02.00+

## Examples

The repository includes a comprehensive example app demonstrating:

1. **Basic Single Date Selection**
2. **Date Range Selection**
3. **Limited Available Dates** (weekdays only)
4. **Pre-selected Date**
5. **Custom Locale** (Arabic)
6. **Weekends Only Range**

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License

```
MIT License

Copyright (c) 2024 ahmad abu kashef

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Support

If you find this library helpful, please ⭐ star the repository!

For issues and feature requests, please use the [GitHub Issues](https://github.com/yourusername/custom-date-picker/issues) page.
