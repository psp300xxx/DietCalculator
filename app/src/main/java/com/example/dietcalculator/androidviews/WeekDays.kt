package com.example.dietcalculator.androidviews

enum class WeekDays {


    Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday;

    companion object {
        fun fromValue(value: Int) = values().first { it.ordinal == value }
    }

    val value: Int
        get() {
            return ordinal
        }


}