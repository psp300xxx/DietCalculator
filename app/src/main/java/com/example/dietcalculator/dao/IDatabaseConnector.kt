package com.example.dietcalculator.dao

import android.content.Context
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.model.Food

interface IDatabaseConnector {

    fun connect(context: Context)

    fun addDelegate(delegate: IDatabaseDelegate)

    fun getFoodEntries()

    fun addFood(food: Food)

    fun downloadDataFromInternet()

    fun addFoodRelation(foodRelation: FoodRelation)

    fun deleteDatabase()

    fun getRelationEntriesFoodInput(list: List<Food>)

    fun getRelationEntriesNameInputs(list: List<String>)

    fun getRelationEntriesIDinputs(list: List<Integer>)


}