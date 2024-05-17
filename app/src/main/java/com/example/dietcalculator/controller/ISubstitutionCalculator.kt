package com.example.dietcalculator.controller

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

interface ISubstitutionCalculator {

    fun compute(foodOriginal: Food, foodReplace: Food, originalQuantityGr: Double, relations: List<FoodRelation>): Double

}