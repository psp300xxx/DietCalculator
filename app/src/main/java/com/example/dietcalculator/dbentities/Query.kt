package com.example.dietcalculator.dbentities

import android.provider.BaseColumns

object Query {

    const val SQL_CREATE_FOOD_TABLE =
        "CREATE TABLE ${FoodDB.FoodEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FoodDB.FoodEntry.COLUMN_NAME} TEXT NOT NULL UNIQUE," +
                "${FoodDB.FoodEntry.COLUMN_CALORIES} REAL NOT NULL," +
                "${FoodDB.FoodEntry.COLUMN_PROTEIN} REAL," +
                "${FoodDB.FoodEntry.COLUMN_CARBO} REAL," +
                "${FoodDB.FoodEntry.COLUMN_FAT} REAL," +
                "${FoodDB.FoodEntry.COLUMN_SALT} REAL," +
                "${FoodDB.FoodEntry.COLUMN_ALCOL} REAL," +
                "${FoodDB.FoodEntry.COLUMN_IS_VEGAN} INTEGER)"

    const val SQL_CREATE_FOOD_RELATION_TABLE = "CREATE TABLE ${FoodRelation.FoodRelationEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FoodRelation.FoodRelationEntry.COLUMN_FOOD_ONE} TEXT NOT NULL," +
            "${FoodRelation.FoodRelationEntry.COLUMN_FOOD_TWO} TEXT NOT NULL," +
            "${FoodRelation.FoodRelationEntry.COLUMN_RATIO} REAL NOT NULL," +
            "FOREIGN KEY(${FoodRelation.FoodRelationEntry.COLUMN_FOOD_ONE}) REFERENCES ${FoodDB.FoodEntry.TABLE_NAME}(${BaseColumns._ID}),"+
            "FOREIGN KEY(${FoodRelation.FoodRelationEntry.COLUMN_FOOD_TWO}) REFERENCES ${FoodDB.FoodEntry.TABLE_NAME}(${BaseColumns._ID}))"

    const val SQL_DELETE_TABLE_TEMPLATE = "DROP TABLE IF EXISTS %s"

    fun createDeleteTableQuery(tableName: String): String {
        return String.format(SQL_DELETE_TABLE_TEMPLATE, tableName)
    }


}