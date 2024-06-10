package com.example.dietcalculator.utility

object AppConstants {

    val PROTEIN_PERCENTAGE = 0.15

    val APP_LOG_TAG = "DietCalculator"


    val OPEN_FOOD_FACTS_ENDPOINT_TEMPLATE = "https://world.openfoodfacts.org/country/%s.json?page=%d&page_size=%d"


    fun getFoodFactsEndpoint(country: String, pageSize: Int=100, page:Int=0): String{
        return OPEN_FOOD_FACTS_ENDPOINT_TEMPLATE.format(country, page, pageSize)
    }

}