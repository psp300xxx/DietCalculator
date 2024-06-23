package com.example.dietcalculator.utility

import com.example.dietcalculator.dbentities.FoodDB
import com.example.dietcalculator.dbentities.FoodRelation
import com.example.dietcalculator.model.DBValue
import com.example.dietcalculator.model.Food
import org.junit.Assert
import org.junit.Test

class UtilityTest {

    private val utility: Utility = Utility

    @Test
    fun test_FoodfromMap(){
        val map = mapOf(
            FoodDB.FoodEntry.COLUMN_NAME to "name",
            FoodDB.FoodEntry.COLUMN_CALORIES to 100.0,
            FoodDB.FoodEntry.COLUMN_PROTEIN to 10.0,
            FoodDB.FoodEntry.COLUMN_FAT to 10.0,
            FoodDB.FoodEntry.COLUMN_CARBO to 10.0,
            FoodDB.FoodEntry.COLUMN_ALCOL to 10.0,
            FoodDB.FoodEntry.COLUMN_SALT to 10.0,
            FoodDB.FoodEntry.COLUMN_IS_VEGAN to true
        )
        val food = utility.fromMap(map, Food::class.java)
        Assert.assertNotNull(food)
    }

    @Test
    fun test_FoodRelationfromMap(){
        val map = mapOf(
            FoodRelation.FoodRelationEntry.COLUMN_FOOD_ONE to "one",
            FoodRelation.FoodRelationEntry.COLUMN_FOOD_TWO to "two",
            FoodRelation.FoodRelationEntry.COLUMN_RATIO to 0.5
        )
        val foodRelation = utility.fromMap(map, com.example.dietcalculator.model.FoodRelation::class.java)
        Assert.assertNotNull(foodRelation)
    }

}