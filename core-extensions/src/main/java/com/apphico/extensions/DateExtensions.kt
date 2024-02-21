package com.apphico.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.milliseconds

private fun getNow() = Instant.now().atZone(ZoneId.systemDefault())

fun getNowDateTime(): LocalDateTime = getNow().toLocalDateTime()

fun getNowDate(): LocalDate = getNow().toLocalDate()

fun getGMTNowMillis() = LocalDateTime.now().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli()

fun YearMonth.formatLong(): String = this.format(DateTimeFormatter.ofPattern("MMMM"))

fun YearMonth.formatLongYear(): String = this.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.getLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.of("GMT")).toLocalDateTime()

fun Long.formatMediumDate(): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(Instant.ofEpochMilli(this).atZone(ZoneId.of("GMT")))

fun Long.formatHours(): String = this.milliseconds.toComponents { hours, minutes, seconds, _ ->
    if(hours > 0) "%02dh %02dm %02ds".format(hours, minutes, seconds) else "%02dm %02ds".format(minutes, seconds)
}

fun LocalDate.formatDayOfWeekDate() = DateTimeFormatter.ofPattern("E, d MMM").format(this).replaceFirstChar(Char::titlecase)

fun LocalDate.formatMediumDate() = DateTimeFormatter.ofPattern("d MMM yyyy").format(this).replaceFirstChar(Char::titlecase)

fun LocalDateTime.formatDateAndTime(): String = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this)

fun LocalDateTime.formatMediumDate(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(this)

fun LocalDateTime.formatShortDate(): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(this)

fun LocalDateTime.formatShortTime(): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)

fun LocalTime.formatShortTime(): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)