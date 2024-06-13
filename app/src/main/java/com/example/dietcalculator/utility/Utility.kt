package com.example.dietcalculator.utility

import com.example.dietcalculator.model.Food
import org.json.JSONArray

object Utility {

    fun formatCountryName(country: String): String {
        return country.lowercase().replace(" ","_")
    }

    fun jsonArrayToFoodList(jsonArray: JSONArray): List<Food>{
        return mutableListOf()
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
