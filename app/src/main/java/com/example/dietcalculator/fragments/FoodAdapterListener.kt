package com.example.dietcalculator.fragments

interface FoodAdapterListener {

    fun onNoShowableObjects(adapter: FoodAdapter)

    fun onHasShowableObjects(adapter: FoodAdapter)

}