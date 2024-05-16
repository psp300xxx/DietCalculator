package com.example.dietcalculator.dbentities

import android.provider.BaseColumns

class FoodRelation {
    object FoodRelationEntry : BaseColumns {
        const val TABLE_NAME = "FoodRelation"
        const val COLUMN_FOOD_ONE = "food_one"
        const val COLUMN_FOOD_TWO = "food_two"
        const val COLUMN_RATIO = "ratio"
    }
}