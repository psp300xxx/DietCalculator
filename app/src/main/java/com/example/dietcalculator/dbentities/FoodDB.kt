package com.example.dietcalculator.dbentities

import android.provider.BaseColumns

object FoodDB {
    object FoodEntry : BaseColumns {
        const val TABLE_NAME = "Food"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORIES = "kcal"
        const val COLUMN_PROTEIN = "proteins"
        const val COLUMN_CARBO = "carbo"
        const val COLUMN_FAT = "fat"
        const val COLUMN_ALCOL = "alcol"
        const val COLUMN_SALT = "salt"
    }
}