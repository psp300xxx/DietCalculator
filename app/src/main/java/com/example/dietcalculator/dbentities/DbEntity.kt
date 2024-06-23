package com.example.dietcalculator.dbentities

import android.provider.BaseColumns
import com.example.dietcalculator.model.DBValue

interface DbEntity {

    fun getColumns(): Array<String> {
        var result = arrayOf(BaseColumns._ID)
        for (field in javaClass.declaredFields) {
            val annotation = field.getAnnotation(DBValue::class.java)
            if(annotation!=null){
                val columnName = annotation.columnName
                result += columnName
            }
        }
        return result
    }

    fun tableName(): String



    fun getColumns(vararg columns: String): Array<String>{
        return getColumns().filter {
            !columns.contains(it)
        }.toTypedArray()
    }
}