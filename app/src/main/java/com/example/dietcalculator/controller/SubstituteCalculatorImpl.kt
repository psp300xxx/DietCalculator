package com.example.dietcalculator.controller

import com.example.dietcalculator.datastructures.ITree
import com.example.dietcalculator.datastructures.TreeImpl
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import java.util.stream.Collectors

object SubstituteCalculatorImpl : ISubstitutionCalculator {

    private fun isFoodInRelation(foodName: String, relation: FoodRelation): Boolean{
        return relation.foodOne==foodName || relation.foodTwo==foodName
    }

    private fun existsDirectRelation(foodOne: String, foodTwo: String, relations: List<FoodRelation>): FoodRelation? {
        for (relation in relations){
            if ( relation.areFoodInRelation(foodOne, foodTwo) ){
                return relation
            }
        }
        return null
    }

    private fun alreadyInTree(food: String, tree: ITree<FoodRelation>?): Boolean {
        var currentNode : ITree<FoodRelation>? = tree
        while( currentNode!=null ){
            val isFoodInRelation: Boolean? = currentNode?.getValue()?.foodOne == food
            if(isFoodInRelation != null && isFoodInRelation){
                return true
            }
            currentNode = currentNode.getParent()
        }
        return false
    }

    private fun computeTree( tree: ITree<FoodRelation>, source: String, target: String , relations: List<FoodRelation>): ITree<FoodRelation>?{
        if( tree.getValue().isFoodInRelation(target) ){
            return tree
        }
        var result: ITree<FoodRelation>? = null
        val sourceRelations = relations.filter { rel -> rel.isFoodInRelation(source) }
        for ( rel in sourceRelations ){
            val relation = rel.getRelationHavingFoodAsOne(source)
            if(alreadyInTree(relation.foodTwo, tree)){
                continue
            }
            val childNode = tree.addChildren( rel.getRelationHavingFoodAsOne(relation.foodTwo) )
            result = computeTree(childNode, relation.foodTwo, target, relations) ?: result
        }
        return result
    }


    private fun buildGraph(source: Food, target: Food, relations: List<FoodRelation>): List<FoodRelation>? {
        val sourceRelations = relations.filter { rel -> rel.isFoodInRelation(source.name) }
        var result: ITree<FoodRelation>? = null
        for( relation in sourceRelations ){
            val sourceRelation = relation.getRelationHavingFoodAsOne(source.name)
            val root: ITree<FoodRelation> = TreeImpl<FoodRelation>( sourceRelation )
            result = computeTree(root, sourceRelation.foodTwo, target.name, relations)
        }
        var all_rel = listOf<FoodRelation>()
        result?.getRoot()?.visitTree({ value -> all_rel = all_rel + value })
        return if( result!=null ) result.leafToRootValues() else null
    }

    override fun compute(
        foodOriginal: Food,
        foodReplace: Food,
        originalQuantityGr: Double,
        relations: List<FoodRelation>
    ): Double {
        var directRelation = existsDirectRelation(foodOriginal.name, foodReplace.name, relations)
        if (directRelation!=null){
            val ratio = directRelation.getRatioFor(foodOriginal.name)
            return originalQuantityGr * ratio
        }
        var relationsDetected = buildGraph(foodOriginal, foodReplace, relations)
        if (relationsDetected==null){
            throw NoFoodRelationException()
        }
        relationsDetected = relationsDetected.asReversed()
        var tmpQuantity = originalQuantityGr
        var currentSource = foodOriginal.name
        for ( relation in relationsDetected){
            val ratio = relation.getRatioFor(currentSource)
            tmpQuantity *= ratio
            currentSource = if (relation.foodOne == currentSource) relation.foodTwo else relation.foodOne
        }
        return tmpQuantity
    }
}