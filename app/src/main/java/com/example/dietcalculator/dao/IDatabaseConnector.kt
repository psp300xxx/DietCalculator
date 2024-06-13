package com.example.dietcalculator.dao

import android.content.Context
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloaderDelegate
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.model.Food

interface IDatabaseConnector {

    fun connect(context: Context)

    fun addDelegate(delegate: IDatabaseDelegate)

    fun removeDelegate(delegate: IDatabaseDelegate)

    fun getFoodEntries()

    fun addFood(food: Food)

    fun downloadDataFromInternet(deleteDb: Boolean=false)

    fun addDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate)

    fun removeDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate)

    fun addFoodRelation(foodRelation: FoodRelation)

    fun deleteDatabase(parallelize: Boolean = false)

    fun recreateDb(parallelize: Boolean = false)

    fun getRelationEntriesFoodInput(list: List<Food>)

    fun getRelationEntriesNameInputs(list: List<String>)

    fun getRelationEntriesIDinputs(list: List<Int>)


}