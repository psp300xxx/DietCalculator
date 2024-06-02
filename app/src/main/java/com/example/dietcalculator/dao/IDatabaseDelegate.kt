package com.example.dietcalculator.dao

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

interface IDatabaseDelegate {

    fun dbConnectedSuccessfully(connector: IDatabaseConnector)

    fun foodDataRetrieved(connector: IDatabaseConnector, list: List<Food>)

    fun foodAdded(connector: IDatabaseConnector, food: Food)

    fun dbDeleted(connector: IDatabaseConnector)

    fun foodRelationAdded(connector: IDatabaseConnector, foodRelation: FoodRelation)

    fun allFoodRelationsRetrieved(connector: IDatabaseConnector, relations: List<FoodRelation>)

    fun filteredFoodRelationsRetrieved(connector: IDatabaseConnector, relations: List<FoodRelation>, foods: List<Food>)

    fun errorRaised(connector: IDatabaseConnector, exception: Throwable)

}