package com.example.dietcalculator.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.dietcalculator.dbentities.DbUtility
import com.example.dietcalculator.dbentities.FoodDB
import com.example.dietcalculator.dbentities.FoodReaderDbHelper
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import java.util.function.Consumer

class SQLLiteConnector() : IDatabaseConnector {

    var database: SQLiteDatabase? = null
    var delegates: List<IDatabaseDelegate>? = null
    var dbHelper: FoodReaderDbHelper? = null

    override fun connect(context: Context) {
        dbHelper = FoodReaderDbHelper(context, false)
        database = dbHelper?.writableDatabase
        notifyDelegates {
            del ->
            del.dbConnectedSuccessfully(this)
        }
    }

    override fun addDelegate(delegate: IDatabaseDelegate) {
        if(this.delegates==null){
            this.delegates = ArrayList<IDatabaseDelegate>()
        }
        this.delegates = this.delegates!! + delegate
    }

    private fun notifyDelegates( operation: Consumer<IDatabaseDelegate>){
        if (this.delegates!=null){
            for( delegate in this.delegates!! ){
                operation.accept(delegate)
            }
        }
    }

    override fun deleteDatabase() {
        var exception: Throwable? = null
        try {
            DbUtility.deleteDatabase(database!!)
        }catch (e: Throwable){
            exception = e
        }finally {
            notifyDelegates {
                delegate ->
                if(exception==null){
                    delegate.dbDeleted(this)
                }
                else {
                    delegate.errorRaised(this, exception)
                }
            }
        }
    }

    override fun getRelationEntriesFoodInput(list: List<Food>) {
        TODO("Not yet implemented")
    }

    override fun getRelationEntriesNameInputs(list: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getRelationEntriesIDinputs(list: List<Integer>) {
        TODO("Not yet implemented")
    }

    override fun getFoodEntries() {

        if (this.database==null){
            return
        }
        val projection = arrayOf(BaseColumns._ID, FoodDB.FoodEntry.COLUMN_NAME, FoodDB.FoodEntry.COLUMN_CALORIES
        , FoodDB.FoodEntry.COLUMN_PROTEIN, FoodDB.FoodEntry.COLUMN_FAT, FoodDB.FoodEntry.COLUMN_ALCOL
        ,FoodDB.FoodEntry.COLUMN_SALT, FoodDB.FoodEntry.COLUMN_CARBO)
        val cursor = database!!.query(
            FoodDB.FoodEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        var result = ArrayList<Food>()
        with(cursor){
            while(cursor.moveToNext()){
                val map = HashMap<String, Any>()
                val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_NAME))
                val kcal = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_CALORIES))
                val proteins = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_PROTEIN))
                val fat = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_FAT))
                val carbo = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_CARBO))
                val salt = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_SALT))
                val alcol = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_ALCOL))
                val food = Food(name = name, carbo = carbo, kcal = kcal, protein = proteins, fat = fat
                , alcol = alcol, salt = salt)
                result.add(food)
            }
        }
        cursor.close()
        notifyDelegates {
            delegate -> delegate.foodDataRetrieved(this, result)
        }
    }

    override fun addFood(food: Food) {
        var exception: Throwable? = null
        try{
            DbUtility.addFood(this.database!!, food)
        }catch (e: Throwable){
            exception = e
        }
        finally {
            notifyDelegates {
                del ->
                if (exception == null){
                    del.foodAdded(this, food)
                }
                else {
                    del.errorRaised(this, exception)
                }
            }
        }
    }

    override fun addFoodRelation(foodRelation: FoodRelation) {
        TODO("Not yet implemented")
    }


}