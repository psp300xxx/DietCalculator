package com.example.dietcalculator.dao.mockconnector

import android.content.Context
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloaderDelegate
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import java.util.Collections

class MockConnector: IDatabaseConnector {

    private var delegates: MutableList<IDatabaseDelegate> = mutableListOf()
    private var foods :MutableList<Food> = mutableListOf()
    private var foodRelations :MutableList<FoodRelation> = mutableListOf()

    companion object {
        val DEFAULT_INIT_NUMBER = 20
    }

    constructor(initNumber: Int){
        this.createFoods(initNumber)
    }

    constructor(): this(DEFAULT_INIT_NUMBER){

    }

    fun createFoods(number: Int){
        for ( i in 0..number-1 ){
            val foodName = String.format("food%d", i)
            val food = Food(name = foodName, 20.0,0.0,0.0,0.0,0.0,0.0)
            this.foods.add(
                food
            )
            this.delegates.forEach {
                it.onFoodAddedToDb(this, food)
            }
        }
        this.delegates.forEach{
                d ->
            d.onFoodDataRetrievingCompleted(this, number)
        }
    }

    override fun connect(context: Context) {
    }

    override fun addDelegate(delegate: IDatabaseDelegate) {
        this.delegates.add(delegate)
    }

    override fun removeDelegate(delegate: IDatabaseDelegate) {
        TODO("Not yet implemented")
    }

    override fun getFoodEntries() {
        for (food in this.foods){
            this.delegates.forEach{
                    d ->
                d.onFoodItemRetrieved(this, food)
            }
        }
        this.delegates.forEach{
            d ->
            d.onFoodDataRetrievingCompleted(this, this.foods.size)
        }
    }

    override fun addFood(food: Food) {
        this.foods.add(food)
        this.delegates.forEach{
            d ->
            d.onFoodAddedToDb(this, food)
        }
    }

    override fun downloadDataFromInternet(deleteDb: Boolean) {
        TODO("Not yet implemented")
    }


    override fun addDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate) {
        TODO("Not yet implemented")
    }

    override fun removeDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate) {
        TODO("Not yet implemented")
    }

    override fun addFoodRelation(foodRelation: FoodRelation) {
        this.foodRelations.add(foodRelation)
        this.delegates.forEach{
            d ->
            d.foodRelationAdded(this, foodRelation)
        }
    }

    override fun deleteDatabase(parallelize: Boolean) {
        this.foods.clear()
        this.foodRelations.clear()
        this.delegates.forEach{
                d ->
            d.dbDeleted(this)
        }
    }

    override fun recreateDb(parallelize: Boolean) {
        TODO("Not yet implemented")
    }


    fun foodNumber(): Int {
        return this.foods.size
    }

    fun clearDb(){
        this.foods.clear()
        this.foodRelations.clear()
        this.delegates.forEach{
                d ->
            d.onDBRecreated(this)
        }
    }

    override fun getRelationEntriesFoodInput(list: List<Food>) {
        val filtered = this.foodRelations.filter {
            r ->
            var result = false
            for (f in list){
                if( r.isFoodInRelation(f.name) ){
                    result = true
                    break
                }
            }
            result
        }
        this.delegates.forEach{
            d ->
            d.filteredFoodRelationsRetrieved(this, filtered, list)
        }
    }

    override fun getRelationEntriesNameInputs(list: List<String>) {
        val foods = list.map { s ->
            Food(name = s, 0.0,0.0,0.0,0.0,0.0,0.0)
        }
        getRelationEntriesFoodInput(foods)
    }

    override fun getRelationEntriesIDinputs(list: List<Int>) {
        this.delegates.forEach{
                d ->
            d.allFoodRelationsRetrieved(this, Collections.unmodifiableList(this.foodRelations))
        }
    }

}