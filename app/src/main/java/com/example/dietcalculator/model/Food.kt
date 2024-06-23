package com.example.dietcalculator.model

import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.dbentities.FoodDB

class Food(): DbEntity {


    @DBValue(columnName = FoodDB.FoodEntry.COLUMN_NAME)
    var name: String = ""
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_CALORIES)
    var kcal: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_PROTEIN)
    var protein: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_FAT)
    var fat: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_CARBO)
    var carbo: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_ALCOL)
    var alcol: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_SALT)
    var salt: Double = 0.0
        public get

    @DBValue(columnName=FoodDB.FoodEntry.COLUMN_IS_VEGAN)
    var isVegan: Boolean = false
        public get


    constructor(name: String, kcal: Double, protein: Double, fat: Double, carbo: Double, alcol: Double, salt: Double): this(){
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


    override fun tableName(): String {
        return FoodDB.FoodEntry.TABLE_NAME
    }






}