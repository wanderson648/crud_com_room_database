package com.wo.orgs.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun fromDouble(amount: Double?): BigDecimal {
        return amount?.let { BigDecimal(amount.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalToDouble(amount: BigDecimal?): Double? {
        return amount?.let { amount.toDouble() }
    }


}