package com.example.dietcalculator.fragments

import com.example.dietcalculator.model.Food

interface IFoodFilter {

    fun isMatching(food: Food): Boolean

}