package com.example.dietcalculator.dao

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

interface IDatabaseDelegate {

    fun dbConnectedSuccessfully(connector: IDatabaseConnector)

    fun onFoodDataRetrievingCompleted(connector: IDatabaseConnector, number:Int)

    fun onFoodItemRetrieved(connector: IDatabaseConnector, food: Food)

    fun onFoodAddedToDb(connector: IDatabaseConnector, food: Food, downloaded: Int? = null, toDownload: Int? = null)

    fun dbDeleted(connector: IDatabaseConnector)

    fun onDBRecreated(connector: IDatabaseConnector)

    fun foodRelationAdded(connector: IDatabaseConnector, foodRelation: FoodRelation)

    fun allFoodRelationsRetrieved(connector: IDatabaseConnector, relations: List<FoodRelation>)

    fun filteredFoodRelationsRetrieved(connector: IDatabaseConnector, relations: List<FoodRelation>, foods: List<Food>)

    fun errorRaised(connector: IDatabaseConnector, exception: Throwable)

}