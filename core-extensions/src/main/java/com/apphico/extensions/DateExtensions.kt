package com.apphico.extensions

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds

private fun getNow() = Instant.now().atZone(ZoneId.systemDefault())

fun getNowDateTime(): LocalDateTime = getNow().toLocalDateTime().truncatedTo(ChronoUnit.MINUTES)

fun getNowDate(): LocalDate = getNow().toLocalDate()

fun getNowTime(): LocalTime = getNow().toLocalTime()

fun getNowGMTMillis() = LocalDateTime.now().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli()

fun YearMonth.formatLong(): String = this.format(DateTimeFormatter.ofPattern("MMMM"))

fun YearMonth.formatLongYear(): String = this.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

fun LocalDate.toMillis() = this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.getLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneId.of("GMT")).toLocalDate()

fun Long.formatMediumDate(): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(Instant.ofEpochMilli(this).atZone(ZoneId.of("GMT")))

fun Long.formatHours(): String = this.milliseconds.toComponents { hours, minutes, seconds, _ ->
    if (hours > 0) "%02dh %02dm %02ds".format(hours, minutes, seconds) else "%02dm %02ds".format(minutes, seconds)
}

fun LocalDate.formatDayAndMonth() = DateTimeFormatter.ofPattern("d MMMM").format(this).replaceFirstChar(Char::titlecase)

fun LocalDate.formatMediumDate() = DateTimeFormatter.ofPattern("d MMM yyyy").format(this).replaceFirstChar(Char::titlecase)

fun LocalDate.formatLongDate() = DateTimeFormatter.ofPattern("d MMMM yyyy").format(this).replaceFirstChar(Char::titlecase)

fun LocalDateTime.formatDateAndTime(): String = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this)

fun LocalDate.formatShortDate(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(this)

fun LocalDateTime.formatMediumDateAndTime(): String {
    val dateFormatted = DateTimeFormatter.ofPattern("d MMM").format(this).replaceFirstChar(Char::titlecase)
    val timeFormatted = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)
    return "$dateFormatted $timeFormatted"
}

fun LocalDateTime.formatMediumDate(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(this)

fun LocalDateTime.formatShortTime(): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)

fun LocalTime.formatShortTime(): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)

fun DayOfWeek.getInt() = when (this) {
    DayOfWeek.SUNDAY -> 1
    DayOfWeek.MONDAY -> 2
    DayOfWeek.TUESDAY -> 3
    DayOfWeek.WEDNESDAY -> 4
    DayOfWeek.THURSDAY -> 5
    DayOfWeek.FRIDAY -> 6
    DayOfWeek.SATURDAY -> 7
}

fun Pair<LocalDate?, LocalTime?>.addMinutesBetween(
    startDate: LocalDate?,
    startTime: LocalTime?,
    endTime: LocalTime
): Pair<LocalDate?, LocalTime?> {
    val (date, time) = this

    val minutesBetween = startTime?.until(endTime, ChronoUnit.MINUTES) ?: 0

    if (startDate != null && date != null) {
        val endDateTime = LocalDateTime.of(date, time).plusMinutes(minutesBetween)
        return Pair(endDateTime.toLocalDate(), endDateTime.toLocalTime())
    } else {
        return Pair(null, time?.plusMinutes(minutesBetween))
    }
}

fun LocalDate.addDaysBetween(startDate: LocalDate?, endDate: LocalDate?): LocalDate? {
    val daysBetween = startDate?.until(endDate)?.days?.toLong() ?: 0
    return this.plusDays(daysBetween)
}