package com.example.dietcalculator.controller

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.Collections

class IsocaloricCalculatorTest {

    private lateinit var calculator: ISubstitutionCalculator

    @Before
    fun setup(){
        calculator = IsocaloricCalculator
    }

    @Test
    fun testSecondFoodIsMoreCaloric(){
        val one  = CalculatorUtility.createFood("one", 100.0)
        val two = CalculatorUtility.createFood("two", 200.0)
        val result = calculator.compute(one, two, 100.0, Collections.emptyList())
        val EXPECTED_RESULT = 50.0
        Assert.assertTrue( CalculatorUtility.closeDouble(EXPECTED_RESULT, result) )
    }

    @Test
    fun testSecondFoodIsLessCaloric(){
        val one  = CalculatorUtility.createFood("one", 100.0)
        val two = CalculatorUtility.createFood("two", 50.0)
        val result = calculator.compute(one, two, 100.0, Collections.emptyList())
        val EXPECTED_RESULT = 200.0
        Assert.assertEquals(EXPECTED_RESULT, result, CalculatorUtility.DEFAULT_EPSILON)
    }

    @Test
    fun testSecondFoodIsZeroCaloric(){
        val one  = CalculatorUtility.createFood("one", 100.0)
        val two = CalculatorUtility.createFood("two", 0.0)
        val result = calculator.compute(one, two, 100.0, Collections.emptyList())
        Assert.assertEquals(Double.POSITIVE_INFINITY, result,CalculatorUtility.DEFAULT_EPSILON)
    }



}