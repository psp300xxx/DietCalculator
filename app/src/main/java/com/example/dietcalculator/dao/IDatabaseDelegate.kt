package com.example.dietcalculator.dao

import com.example.dietcalculator.model.Food

interface IDatabaseDelegate {

    fun dbConnectedSuccessfully(connector: IDatabaseConnector)

    fun foodDataRetrieved(connector: IDatabaseConnector, list: List<Food>)

    fun errorRaised(connector: IDatabaseConnector, exception: Throwable)

}