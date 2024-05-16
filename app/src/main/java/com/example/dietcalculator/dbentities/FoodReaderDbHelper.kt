package com.example.dietcalculator.dbentities

import android.content.Context
import android.database.sqlite.*

class FoodReaderDbHelper(context: Context, val hasToDrop: Boolean=false) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        if(this.hasToDrop){
            println("Deleting DB")
            db.execSQL(Query.createDeleteTableQuery(FoodDB.FoodEntry.TABLE_NAME))
            db.execSQL(Query.createDeleteTableQuery(FoodRelation.FoodRelationEntry.TABLE_NAME))
        }
        println("Creating database ${DATABASE_NAME}")
        println("Executing query: ${Query.SQL_CREATE_FOOD_TABLE}")
        db.execSQL(Query.SQL_CREATE_FOOD_TABLE)
        println("Executing query: ${Query.SQL_CREATE_FOOD_RELATION_TABLE}")
        db.execSQL(Query.SQL_CREATE_FOOD_RELATION_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(Query.createDeleteTableQuery(FoodDB.FoodEntry.TABLE_NAME))
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FoodDB.db"
    }
}