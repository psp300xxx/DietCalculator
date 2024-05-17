package com.example.dietcalculator.model

class FoodRelation(val foodOne: String, val foodTwo: String, val ratio: Double) {

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

}