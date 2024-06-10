package com.example.dietcalculator.dbentities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.dietcalculator.model.DBValue
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.utility.Utility.toInt

object DbUtility {

    private var TABLE_NAMES = arrayOf(FoodDB.FoodEntry.TABLE_NAME, FoodRelation.FoodRelationEntry.TABLE_NAME)

    fun addRow(database: SQLiteDatabase, entity: DbEntity): Long{
        val values = ContentValues()
        for(field in entity.javaClass.declaredFields){
            val annotation = field.getAnnotation(DBValue::class.java)
            annotation?.let {
                field.isAccessible = true
                var value = field.get(entity)
                values.put(annotation.columnName, value)
            }
        }
        var result: Long = -1
        database?.let {
            result = it.insert(entity.tableName(), null, values)
        }
        return result
    }

    fun deleteDatabase(database: SQLiteDatabase){
        for (table in TABLE_NAMES){
            database.execSQL( Query.createDeleteTableQuery(table) )
        }
    }

    fun createDatabase(context: Context){

    }

}

private fun ContentValues.put(columnName: String, value: Any?) {
    if( value is Boolean ){
        this.put(columnName, value.toInt())
    }
    else if (value is String){
        this.put(columnName, value)
    }
    else if (value is Double){
        this.put(columnName, value)
    }
    else if (value is Int){
        this.put(columnName, value)
    }
    else{
        throw IllegalArgumentException()
    }
}
