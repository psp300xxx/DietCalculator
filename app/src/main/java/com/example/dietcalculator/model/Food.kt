package com.example.dietcalculator.model

class Food {

    var name: String
    var kcal: Double
    var protein: Double
    var fat: Double
    var carbo: Double
    var alcol: Double
    var salt: Double
    var isVegan: Boolean

    constructor(name: String, kcal: Double, protein: Double, fat: Double, carbo: Double, alcol: Double, salt: Double){
        this.alcol = alcol
        this.salt = salt
        this.carbo = carbo
        this.fat = fat
        this.protein = protein
        this.kcal = kcal
        this.name = name
        this.isVegan = false
    }

    constructor(name: String, kcal: Double, protein: Double, fat: Double, carbo: Double, alcol: Double, salt: Double, isVegan:Boolean): this(name, kcal, protein, fat, carbo, alcol, salt){
        this.isVegan = isVegan
    }

    override fun toString(): String {
        return "Food(name='$name', kcal=$kcal, protein=$protein, fat=$fat, carbo=$carbo, alcol=$alcol, salt=$salt)"
    }


}