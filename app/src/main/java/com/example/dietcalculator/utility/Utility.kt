package com.example.dietcalculator.utility

import android.database.Cursor
import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.model.DBValue
import com.example.dietcalculator.model.Food
import org.json.JSONArray
import java.lang.reflect.Field

object Utility {



    val CURSOR_TYPES_MAP: Map<Int, Class<out Any>>   =     mapOf( Cursor.FIELD_TYPE_BLOB to String::class.java,
    Cursor.FIELD_TYPE_FLOAT to Double::class.java, Cursor.FIELD_TYPE_INTEGER to Int::class.java,
    Cursor.FIELD_TYPE_STRING to String::class.java, Cursor.FIELD_TYPE_NULL to Any::class.java)



    fun formatCountryName(country: String): String {
        return country.lowercase().replace(" ","_")
    }

    fun <T> Cursor.get(columnName: String, type: Class<T>, columnIndex: Int? = null): T {
        val realColumnIndex = if (columnIndex != null) columnIndex else this.getColumnIndexOrThrow(columnName)
        return when (type) {
            Int::class.java -> getInt(realColumnIndex) as T
            Long::class.java -> getLong(realColumnIndex) as T
            Double::class.java -> getDouble(realColumnIndex) as T
            String::class.java -> getString(realColumnIndex) as T
            else -> {throw IllegalArgumentException("Type:'${type.name}' not supported")}
        }
    }

    fun <T> Cursor.getAny(columnName: String): T {
        val columnIndex = this.getColumnIndexOrThrow(columnName)
        val type = this.getType(columnIndex)
        val clazz = CURSOR_TYPES_MAP[type]
        return get(columnName, clazz!!, columnIndex) as T
    }

    fun Field.set(obj: Any, value: Any, setAccessible: Boolean) {
        if( setAccessible ){
            this.isAccessible = true
        }
        if( this.type == Boolean::class.java && value is Int ){
            this.set(obj, value.toBoolean())
        }
        else{
            this.set(obj, value)
        }
        if( setAccessible ){
            this.isAccessible = false
        }
    }

    fun fromMap(map: Map<String, Any>, dbEntityClass: Class<out DbEntity>): DbEntity {
        val result = dbEntityClass.newInstance()
        for (field in dbEntityClass.declaredFields){
            val annotation = field.getAnnotation(DBValue::class.java)
            if (annotation != null) {
                val value = map[annotation.columnName]
                if (value != null) {
                    field.set(result, value, true)
                }
                else {
                    throw IllegalArgumentException("Column '${annotation.columnName}' not found in map")
                }
            }
        }
        return result
    }


    fun Cursor.getAnyMap(columns: Array<String>): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        for (column in columns) {
            map[column] = getAny(column)
        }
        return map
    }


    fun jsonArrayToFoodList(jsonArray: JSONArray): List<Food>{
        TODO()
    }

    inline fun <T : Any, R : Any> letIfAllNotNull(vararg arguments: T?, block: (List<Any>) -> R): R? {
        return if (arguments.any { it == null }) {
            null
        } else {
            block( arguments.filterNotNull().toList() )
        }
    }

    fun Int.toBoolean():Boolean{
        if(this==0){
            return false
        }
        return true
    }

    fun Boolean.toInt(): Int{
        if (this){
            return 1
        }
        return 0
    }


}

fun <T> MutableList<T>.add(vararg args: T){
    for (arg in args){
        this.add(arg)
    }
}
