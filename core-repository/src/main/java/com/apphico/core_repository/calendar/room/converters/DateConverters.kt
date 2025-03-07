package com.apphico.core_repository.calendar.room.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.LocalTime

class DateConverters {
    @TypeConverter
    fun localTimeToString(localTime: LocalTime?): String? = localTime?.toString()

    @TypeConverter
    fun fromLocalTime(localTime: String?): LocalTime? = localTime?.let { LocalTime.parse(it) }

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime?): String? = localDateTime?.toString()

    @TypeConverter
    fun fromLocalDateTime(localDateTime: String?): LocalDateTime? = localDateTime?.let { LocalDateTime.parse(it) }
}