package com.example.dietcalculator.utility

object Utility {

    fun formatCountryName(country: String): String {
        return country.lowercase().replace(" ","_")
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