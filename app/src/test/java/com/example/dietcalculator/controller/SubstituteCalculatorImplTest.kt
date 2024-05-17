package com.example.dietcalculator.controller

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SubstituteCalculatorImplTest {

    var calculator: ISubstitutionCalculator? = null

    @Before
    fun setup(){
        calculator = SubstituteCalculatorImpl
    }

    private fun createFood(name: String, calories: Double): Food {
        return Food(name = name, kcal = calories, protein = 0.0, carbo = 0.0, fat = 0.0, alcol = 0.0, salt = 0.0)
    }

    private fun createRelation(source: String, target: String, ratio: Double): FoodRelation{
        return FoodRelation(foodOne = source, foodTwo = target, ratio= ratio)
    }

    @Test
    fun isCalculatorInitialised(){
        Assert.assertEquals(calculator?.javaClass, SubstituteCalculatorImpl.javaClass)
    }

    @Test
    fun computeSimpleRatio(){
        var foodOne = createFood("one", 100.0)
        var foodTwo = createFood("two", 120.0)
        var relations = arrayListOf( createRelation(foodOne.name, foodTwo.name, 2.0) )
        val result = calculator?.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 100.0
        Assert.assertEquals(EXPECTED_RESULT, result)
    }

    @Test
    fun computeRatioWithGraph(){
        var foodOne = createFood("one", 100.0)
        var foodTwo = createFood("two", 120.0)
        var foodBridge = createFood("bridge", 150.0)
        var relations = arrayListOf( createRelation(foodOne.name, foodBridge.name, 2.0), createRelation(foodBridge.name, foodTwo.name, 1.5) )
        val result = calculator?.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 150.0
        Assert.assertEquals(EXPECTED_RESULT, result)
    }

    @Test
    fun computeRatioWithBigGraph(){
        var foodOne = createFood("one", 100.0)
        var foodTwo = createFood("two", 120.0)
        var foodBridge1 = createFood("bridge1", 150.0)
        var foodBridge2 = createFood("bridge2", 150.0)
        var foodBridge3 = createFood("bridge3", 150.0)
        var relations = arrayListOf( createRelation(foodOne.name, foodBridge1.name, 2.0), createRelation(foodBridge1.name, foodBridge2.name, 2.0),
            createRelation(foodBridge2.name, foodBridge3.name, 2.0), createRelation(foodBridge3.name, foodTwo.name, 2.0))
        val result = calculator?.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 800.0
        Assert.assertEquals(EXPECTED_RESULT, result)
    }

}