package com.example.dietcalculator.controller

import com.example.dietcalculator.controller.CalculatorUtility.createFood
import com.example.dietcalculator.controller.CalculatorUtility.createRelation
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SubstituteCalculatorImplTest {

    lateinit var calculator: ISubstitutionCalculator

    @Before
    fun setup(){
        calculator = SubstituteCalculatorImpl
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
        val result = calculator.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 100.0
        Assert.assertEquals(EXPECTED_RESULT, result, CalculatorUtility.DEFAULT_EPSILON)
    }

    @Test
    fun computeRatioWithGraph(){
        var foodOne = createFood("one", 100.0)
        var foodTwo = createFood("two", 120.0)
        var foodBridge = createFood("bridge", 150.0)
        var relations = arrayListOf( createRelation(foodOne.name, foodBridge.name, 2.0), createRelation(foodBridge.name, foodTwo.name, 1.5) )
        val result = calculator.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 150.0
        Assert.assertEquals(EXPECTED_RESULT, result, CalculatorUtility.DEFAULT_EPSILON)
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
        val result = calculator.compute(foodOne, foodTwo, 50.0, relations)
        val EXPECTED_RESULT = 800.0
        Assert.assertEquals(EXPECTED_RESULT, result, CalculatorUtility.DEFAULT_EPSILON)
    }

}