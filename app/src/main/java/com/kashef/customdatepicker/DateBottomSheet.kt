package com.customdatepicker

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object CustomDatePickerColors {
    val Primary = Color(0xFF2196F3)
    val PrimaryContainer = Color(0xFF2196F3)
    val OnPrimary = Color.White
    val OnPrimaryContainer = Color.White
    val Background = Color.White
    val OnBackground = Color.Black
    val Surface = Color.White
    val OnSurface = Color.Black
    val Disabled = Color(0xFF9E9E9E)
    val Transparent = Color.Transparent
    val ScrimColor = Color.Black.copy(alpha = 0.32f)
}

// Custom typography
object CustomDatePickerTypography {
    val HeaderMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = CustomDatePickerColors.Primary
    )
    val BodyMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = CustomDatePickerColors.OnSurface
    )
    val BodySmall = androidx.compose.ui.text.TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = CustomDatePickerColors.OnSurface
    )
    val DayOfWeek = androidx.compose.ui.text.TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = CustomDatePickerColors.Primary
    )
    val ButtonText = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = CustomDatePickerColors.OnPrimary
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    availableDates: List<LocalDate>,
    onDismiss: () -> Unit,
    onSaveDate: (String) -> Unit,
    initialDate: String? = null,
    isMultiSelect: Boolean = false,
    saveButtonText: String = "Save",
    locale: Locale = Locale.getDefault()
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val localFocusManager = LocalFocusManager.current

    // State variables to hold the selected start and end dates
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    ModalBottomSheet(
        scrimColor = CustomDatePickerColors.ScrimColor,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        contentColor = CustomDatePickerColors.Background,
        containerColor = CustomDatePickerColors.Background,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                )
        ) {
            // Close button
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(horizontal = 32.dp)
                        .size(20.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = CustomDatePickerColors.Primary,
                )
            }

            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column {
                    val selectedDate = remember {
                        mutableStateOf(
                            runCatching {
                                initialDate?.let { LocalDate.parse(it) }
                            }.getOrElse { availableDates.firstOrNull() }
                        )
                    }

                    if (isMultiSelect) {
                        CustomDateRangePicker(
                            availableDates = availableDates,
                            selectedStartDate = selectedStartDate,
                            selectedEndDate = selectedEndDate,
                            onStartDateSelected = { date ->
                                selectedStartDate = date
                                if (selectedEndDate != null && date.isAfter(selectedEndDate)) {
                                    selectedEndDate = null
                                }
                            },
                            onEndDateSelected = { date ->
                                selectedEndDate = date
                            },
                            locale = locale
                        )
                    } else {
                        CustomDatePicker(
                            availableDates = availableDates,
                            selectedDate = selectedDate.value ?: availableDates.first(),
                            onDateSelected = { newDate ->
                                selectedDate.value = newDate
                            },
                            locale = locale,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    // Save button
                    Button(
                        onClick = {
                            if (isMultiSelect) {
                                if (selectedStartDate != null && selectedEndDate != null) {
                                    onSaveDate("$selectedStartDate,$selectedEndDate")
                                } else {
                                    selectedStartDate?.let { onSaveDate(it.toString()) }
                                }
                            } else {
                                selectedDate.value?.let { onSaveDate(it.toString()) }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomDatePickerColors.Primary,
                            contentColor = CustomDatePickerColors.OnPrimary
                        )
                    ) {
                        Text(
                            text = saveButtonText,
                            style = CustomDatePickerTypography.ButtonText
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    availableDates: List<LocalDate>,
    locale: Locale = Locale.getDefault(),
    modifier: Modifier = Modifier,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val daysOfWeek = getDaysOfWeekNames(locale)
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    val calendarDays = (0..41).map { firstDayOfWeek.plusDays(it.toLong()) }

    Column(
        modifier = modifier
            .background(CustomDatePickerColors.Background)
    ) {
        // Header: Display month and year with navigation buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = CustomDatePickerColors.Primary
                )
            }

            val monthFormatter = DateTimeFormatter.ofPattern("MMMM", locale)
            val formattedMonth = currentMonth.format(monthFormatter)
            Text(
                text = "${formattedMonth.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(locale) else it.toString()
                }} ${currentMonth.year}",
                style = CustomDatePickerTypography.HeaderMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = CustomDatePickerColors.Primary
                )
            }
        }

        // Days of week header
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = CustomDatePickerTypography.DayOfWeek,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > 100f) {
                                currentMonth = currentMonth.minusMonths(1)
                            } else if (offsetX < -100f) {
                                currentMonth = currentMonth.plusMonths(1)
                            }
                            offsetX = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    }
                }
                .fillMaxWidth(),
            content = {
                items(calendarDays) { day ->
                    SingleDateDayItem(
                        day = day,
                        isSelected = day == selectedDate,
                        isInCurrentMonth = day.month == currentMonth.month,
                        isAvailable = availableDates.contains(day),
                        onClick = {
                            if (availableDates.contains(day)) {
                                onDateSelected(day)
                            }
                        }
                    )
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleDateDayItem(
    day: LocalDate,
    isSelected: Boolean,
    isInCurrentMonth: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = when {
        isSelected -> CustomDatePickerColors.PrimaryContainer
        !isAvailable -> CustomDatePickerColors.Transparent
        else -> CustomDatePickerColors.Transparent
    }

    val textColor = when {
        isAvailable && !isSelected -> CustomDatePickerColors.OnSurface
        isSelected -> CustomDatePickerColors.OnPrimaryContainer
        !isAvailable -> CustomDatePickerColors.Disabled
        isInCurrentMonth -> CustomDatePickerColors.OnSurface
        else -> CustomDatePickerColors.Disabled
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor)
            .clickable(onClick = onClick.takeIf { isAvailable } ?: {}),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            color = textColor,
            style = CustomDatePickerTypography.BodyMedium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDateRangePicker(
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    availableDates: List<LocalDate>,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    locale: Locale = Locale.getDefault(),
    modifier: Modifier = Modifier,
) {
    fun isRangeAvailable(startDate: LocalDate, endDate: LocalDate): Boolean {
        return generateSequence(startDate) { date -> date.plusDays(1) }
            .takeWhile { it <= endDate }
            .all { availableDates.contains(it) }
    }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val daysOfWeek = getDaysOfWeekNames(locale)
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    val calendarDays = (0..41).map { firstDayOfWeek.plusDays(it.toLong()) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(CustomDatePickerColors.Background)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = CustomDatePickerColors.Primary
                )
            }

            val monthFormatter = DateTimeFormatter.ofPattern("MMMM", locale)
            val formattedMonth = currentMonth.format(monthFormatter)

            Text(
                text = "${formattedMonth.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(locale) else it.toString()
                }} ${currentMonth.year}",
                style = CustomDatePickerTypography.HeaderMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = CustomDatePickerColors.Primary
                )
            }
        }

        // Days of week
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = CustomDatePickerTypography.DayOfWeek,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > 100f) {
                                currentMonth = currentMonth.minusMonths(1)
                            } else if (offsetX < -100f) {
                                currentMonth = currentMonth.plusMonths(1)
                            }
                            offsetX = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    }
                }
                .fillMaxWidth(),
            content = {
                items(calendarDays) { day ->
                    RangeDateDayItem(
                        day = day,
                        isStartSelected = day == selectedStartDate,
                        isEndSelected = day == selectedEndDate,
                        isInRange = selectedStartDate != null && selectedEndDate != null &&
                                day.isAfter(selectedStartDate) && day.isBefore(selectedEndDate),
                        isAvailable = availableDates.contains(day),
                        onClick = {
                            if (availableDates.contains(day)) {
                                when {
                                    selectedStartDate == null -> onStartDateSelected(day)
                                    selectedEndDate == null && day.isAfter(selectedStartDate) -> {
                                        if (isRangeAvailable(selectedStartDate, day)) {
                                            onEndDateSelected(day)
                                        } else {
                                            onStartDateSelected(day)
                                            onEndDateSelected(null)
                                        }
                                    }
                                    else -> {
                                        onStartDateSelected(day)
                                        onEndDateSelected(null)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RangeDateDayItem(
    day: LocalDate,
    isStartSelected: Boolean,
    isEndSelected: Boolean,
    isInRange: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = when {
        isStartSelected || isEndSelected -> CustomDatePickerColors.PrimaryContainer
        isInRange -> CustomDatePickerColors.PrimaryContainer.copy(alpha = 0.3f)
        else -> CustomDatePickerColors.Transparent
    }

    val textColor = when {
        isInRange -> CustomDatePickerColors.OnPrimaryContainer
        isStartSelected || isEndSelected -> CustomDatePickerColors.OnPrimaryContainer
        isAvailable -> CustomDatePickerColors.OnSurface
        !isAvailable -> CustomDatePickerColors.Disabled
        else -> CustomDatePickerColors.OnSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor)
            .clickable(onClick = onClick.takeIf { isAvailable } ?: {}),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            color = textColor,
            style = CustomDatePickerTypography.BodyMedium
        )
    }
}

// Helper function to get localized day names
private fun getDaysOfWeekNames(locale: Locale): List<String> {
    return listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    // You can enhance this to return localized day names based on the locale
}