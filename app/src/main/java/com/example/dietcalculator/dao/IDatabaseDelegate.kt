package com.example.dietcalculator.dao

import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

interface IDatabaseDelegate {

    fun dbConnectedSuccessfully(connector: IDatabaseConnector)

    fun onItemsRetrieved(connector: IDatabaseConnector,type: Class<out DbEntity>, count: Int)

    fun onItemRetrieved(connector: IDatabaseConnector, item: DbEntity, downloaded: Int? = null, toDownload: Int? = null)

    fun onItemAddedToDb(connector: IDatabaseConnector, item: DbEntity, downloaded: Int? = null, toDownload: Int? = null)

    fun dbDeleted(connector: IDatabaseConnector)

    fun onDBRecreated(connector: IDatabaseConnector)


    fun errorRaised(connector: IDatabaseConnector, exception: Throwable)

}