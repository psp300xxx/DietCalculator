package com.example.dietcalculator.controller

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

object IsocaloricCalculator: ISubstitutionCalculator {
    override fun compute(
        foodOriginal: Food,
        foodReplace: Food,
        originalQuantityGr: Double,
        relations: List<FoodRelation>
    ): Double {
        val ratio = foodOriginal.kcal / foodReplace.kcal
        return originalQuantityGr * ratio
    }
}