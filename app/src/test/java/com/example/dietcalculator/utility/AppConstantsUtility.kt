package com.example.dietcalculator.utility

import org.junit.Assert
import org.junit.Test

class AppConstantsUtility {

    @Test
    fun endpointWithCountryComputed(){
        val EXPECTED_RESULT = "https://world.openfoodfacts.org/country/italy.json"
        val result = AppConstants.getFoodFactsEndpoint("italy")
        Assert.assertEquals(EXPECTED_RESULT, result)
    }

}