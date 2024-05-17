package com.example.dietcalculator.dbentities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.dietcalculator.model.Food

object DbUtility {

    private var TABLE_NAMES = arrayOf(FoodDB.FoodEntry.TABLE_NAME, FoodRelation.FoodRelationEntry.TABLE_NAME)

    fun addFood(database: SQLiteDatabase, food: Food): Long? {
        val values = ContentValues()
        values.put("name", food.name)
        values.put("alcol", food.alcol)
        values.put("fat", food.fat)
        values.put("carbo", food.carbo)
        values.put("kcal", food.kcal)
        values.put("proteins", food.protein)
        values.put("salt", food.salt)
        return database?.insert(FoodDB.FoodEntry.TABLE_NAME, null, values)
    }

    fun deleteDatabase(database: SQLiteDatabase){
        for (table in TABLE_NAMES){
            database.execSQL( Query.createDeleteTableQuery(table) )
        }
    }

    fun createDatabase(context: Context){

    }

}