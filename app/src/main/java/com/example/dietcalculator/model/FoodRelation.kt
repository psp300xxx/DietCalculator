package com.example.dietcalculator.model

import com.example.dietcalculator.dbentities.DbEntity

class FoodRelation(): DbEntity {

    @DBValue(columnName = com.example.dietcalculator.dbentities.FoodRelation.FoodRelationEntry.COLUMN_FOOD_ONE)
    private var _foodOne: String = ""

    val foodOne: String
        get() = _foodOne

    @DBValue(columnName = com.example.dietcalculator.dbentities.FoodRelation.FoodRelationEntry.COLUMN_FOOD_TWO)
    private var _foodTwo: String = ""

    val foodTwo: String
        get() = _foodTwo

    @DBValue(columnName = com.example.dietcalculator.dbentities.FoodRelation.FoodRelationEntry.COLUMN_RATIO)
    private var _ratio: Double = 0.0

    val ratio: Double
        get() = _ratio

    constructor(foodOne: String, foodTwo: String, ratio: Double): this() {
        this._foodOne = foodOne
        this._foodTwo = foodTwo
        this._ratio = ratio
    }

    private var oppositeRelation: FoodRelation? = null

    fun getOppositeRelation(): FoodRelation{
        if(oppositeRelation==null){
            this.oppositeRelation = FoodRelation(foodOne= foodTwo, foodTwo= foodOne, ratio = 1.0/ratio)
            this.oppositeRelation!!.oppositeRelation = this
        }
        return this.oppositeRelation!!
    }

    fun getRelationHavingFoodAsOne(food: String): FoodRelation {
        if( food == foodOne ){
            return this
        }
        else if( food == foodTwo ){
            return getOppositeRelation()
        }
        throw IllegalArgumentException()
    }

    fun getRatioFor(food: String): Double {
        if( food == foodOne ){
            return ratio
        }
        else if ( food == foodTwo ){
            return 1.0/ratio
        }
        throw IllegalArgumentException()
    }

    fun isFoodInRelation(food: String): Boolean {
        if( food == foodOne || food == foodTwo ){
            return true;
        }
        return false;
    }

    fun areFoodInRelation( foodOne: String, foodTwo: String ): Boolean{
        return isFoodInRelation(foodOne) && isFoodInRelation(foodTwo)
    }

    override fun tableName(): String {
        return com.example.dietcalculator.dbentities.FoodRelation.FoodRelationEntry.TABLE_NAME
    }

}