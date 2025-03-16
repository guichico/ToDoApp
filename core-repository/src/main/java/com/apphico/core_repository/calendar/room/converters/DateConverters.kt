package com.apphico.core_repository.calendar.room.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class DateConverters {
    @TypeConverter
    fun localTimeToString(localTime: LocalTime?): String? = localTime?.toString()

    @TypeConverter
    fun fromLocalTime(localTime: String?): LocalTime? = localTime?.let { LocalTime.parse(it) }

    @TypeConverter
    fun localDateToString(localDate: LocalDate?): String? = localDate?.toString()

    @TypeConverter
    fun fromLocalDate(localDate: String?): LocalDate? = localDate?.let { LocalDate.parse(it) }
}