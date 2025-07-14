package com.apphico.database.room.converters

import androidx.room.TypeConverter

class ListConverters {
    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String = intList.toString()

    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val result = ArrayList<Int>()
        val split = stringList.replace("[", "").replace("]", "").replace(" ", "").split(",")

        split.forEach { n ->
            try {
                result.add(n.toInt())
            } catch (_: Exception) {
            }
        }

        return result
    }
}