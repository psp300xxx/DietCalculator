package com.example.dietcalculator.dbentities

class FoodSavingException(private val list: Set<String>): Exception() {

    override val message: String?
        get() = "Unable to store the following food: '%s'".format(list)
}