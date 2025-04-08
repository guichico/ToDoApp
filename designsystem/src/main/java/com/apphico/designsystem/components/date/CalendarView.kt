package com.apphico.designsystem.components.date

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.theme.DisabledColor
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.extensions.formatLong
import com.apphico.extensions.formatLongYear
import com.apphico.extensions.getNowDate
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarView(
    calendarListOffsetY: State<Float>,
    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit
) {
    AnimatedVisibility(
        visible = isCalendarExpanded.value,
        enter = slideInVertically(tween()) + expandVertically() + fadeIn(),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        val coroutineScope = rememberCoroutineScope()

        val currentMonth = remember { selectedDate.value.yearMonth }
        val startMonth = remember { currentMonth.minusMonths(100) }
        val endMonth = remember { currentMonth.plusMonths(100) }
        val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

        val calendarState = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeek
        )

        LaunchedEffect(selectedDate.value) {
            calendarState.animateScrollToMonth(selectedDate.value.yearMonth)
        }

        val offsetY = with(LocalDensity.current) { (calendarListOffsetY.value * -1).toDp() }

        HorizontalCalendar(
            modifier = Modifier
                .offset(y = offsetY)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    start = ToDoAppTheme.spacing.small,
                    end = ToDoAppTheme.spacing.small,
                    bottom = ToDoAppTheme.spacing.small
                ),
            state = calendarState,
            dayContent = {
                CalendarDay(
                    calendarDay = it,
                    selectedDate = selectedDate,
                    onSelectedDateChanged = onSelectedDateChanged
                )
            },
            monthHeader = { month ->
                CalendarMonth(
                    month = month,
                    onPreviousMonthClicked = {
                        coroutineScope.launch {
                            calendarState.scrollToMonth(month.yearMonth.minusMonths(1))
                        }
                    },
                    onNextMonthClicked = {
                        coroutineScope.launch {
                            calendarState.scrollToMonth(month.yearMonth.plusMonths(1))
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun CalendarDay(
    calendarDay: CalendarDay,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit
) {
    var boxModifier = Modifier
        .padding(
            start = ToDoAppTheme.spacing.small,
            end = ToDoAppTheme.spacing.small,
            bottom = ToDoAppTheme.spacing.extraSmall
        )
        .aspectRatio(1f)

    val shape = CircleShape
    var textColor = Color.Unspecified

    val isDateSelected = selectedDate.value.isEqual(calendarDay.date)
    val isToday = getNowDate().isEqual(calendarDay.date)
    val isSameMonth = calendarDay.position == DayPosition.MonthDate

    if (isDateSelected || isToday) {
        textColor = if (isDateSelected) White else Color.Unspecified

        val bgColor = if (isDateSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

        boxModifier = boxModifier
            .shadow(2.dp, shape, true)
            .background(bgColor, shape)
            .border(
                shape = shape,
                border = BorderStroke(width = 1.dp, color = bgColor)
            )
    } else {
        boxModifier = boxModifier
            .shadow(0.dp, shape, true)
    }

    Box(
        modifier = boxModifier
            .clickable(
                enabled = isSameMonth,
                onClick = { onSelectedDateChanged(calendarDay.date) }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = calendarDay.date.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSameMonth) textColor else DisabledColor
        )
    }
}

@Composable
private fun CalendarMonth(
    month: CalendarMonth,
    onPreviousMonthClicked: () -> Unit,
    onNextMonthClicked: () -> Unit
) {
    // You may want to use `remember {}` here so the mapping is not done
    // every time as the days of week order will never change unless
    // you set a new value for `firstDayOfWeek` in the state.
    val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }

    Column {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icArrowLeft,
                onClick = onPreviousMonthClicked
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = ToDoAppTheme.spacing.extraLarge)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = if (month.yearMonth.year == getNowDate().yearMonth.year) month.yearMonth.formatLong() else month.yearMonth.formatLongYear(),
                style = MaterialTheme.typography.titleMedium
            )
            ToDoAppIconButton(
                icon = ToDoAppIcons.icArrowRight,
                onClick = onNextMonthClicked
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = ToDoAppTheme.spacing.small),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CalendarViewPreview() {
    ToDoAppTheme {
        CalendarView(
            calendarListOffsetY = remember { mutableFloatStateOf(0f) },
            isCalendarExpanded = remember { mutableStateOf(true) },
            selectedDate = remember { mutableStateOf(getNowDate()) },
            onSelectedDateChanged = {}
        )
    }
}