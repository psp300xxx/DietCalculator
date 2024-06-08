package com.example.dietcalculator.fragments

import com.example.dietcalculator.model.Food
import java.util.concurrent.locks.Condition
import java.util.function.Predicate

class FoodFilter: IFoodFilter {

    private var conditions: MutableList<Predicate<Food>> = mutableListOf()
    private var nameConditions: MutableList<Predicate<String>> = mutableListOf()
    override fun isMatching(food: Food): Boolean {
        var result = true
        for( condition in conditions ){
            result = result && condition.test(food)
            if(!result){
                return false
            }
        }
        for( condition in nameConditions ){
            result = result && condition.test(food.name)
            if(!result){
                return false
            }
        }
        return result
    }

    fun addCondition(condition: Predicate<Food>){
        this.conditions.add(condition)
    }

    fun addNameCondition(condition: Predicate<String>){
        this.nameConditions.add(condition)
    }

    fun removeCondition(condition : Predicate<Food>){
        this.conditions.remove(condition)
    }

    fun clearConditions(){
        this.conditions.clear()
        this.nameConditions.clear()
    }

    fun clearNameConditions(){
        this.nameConditions.clear()
    }



}