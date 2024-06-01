package com.example.dietcalculator.controller

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

object CalculatorUtility {

    val DEFAULT_EPSILON = 1E-6

    fun createFood(name: String, calories: Double): Food {
        return Food(name = name, kcal = calories, protein = 0.0, carbo = 0.0, fat = 0.0, alcol = 0.0, salt = 0.0)
    }

    fun createRelation(source: String, target: String, ratio: Double): FoodRelation {
        return FoodRelation(foodOne = source, foodTwo = target, ratio= ratio)
    }

    fun closeDouble(a: Double, b: Double, epsilon: Double): Boolean{
        val diff = Math.abs(a-b)
        return diff <= epsilon
    }

    fun closeDouble(a: Double, b: Double): Boolean{
        return closeDouble(a,b, DEFAULT_EPSILON)
    }

}