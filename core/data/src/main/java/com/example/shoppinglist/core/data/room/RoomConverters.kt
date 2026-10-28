package com.example.shoppinglist.core.data.room

import androidx.room.TypeConverter
import java.util.Date

class RoomConverters {

    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let(::Date)
    }
}
