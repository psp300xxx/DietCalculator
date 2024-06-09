package com.example.dietcalculator.dao.sqliteconnector

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloader
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloaderDelegate
import com.example.dietcalculator.dao.fooddatadownloader.OpenFoodDataDownloader
import com.example.dietcalculator.dbentities.DbUtility
import com.example.dietcalculator.dbentities.FoodDB
import com.example.dietcalculator.dbentities.FoodReaderDbHelper
import com.example.dietcalculator.dbentities.FoodSavingException
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.utility.AppConstants
import com.example.dietcalculator.utility.Utility
import com.example.dietcalculator.utility.Utility.toBoolean
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.function.Consumer


class SQLLiteConnector() : IDatabaseConnector, IRemoteFoodDataDownloaderDelegate {


    var database: SQLiteDatabase? = null
    var delegates: MutableList<IDatabaseDelegate>? = null
    var dbHelper: FoodReaderDbHelper? = null
    private val daemonThread: SQLiteThread = SQLiteThread()
    private val remoteFoodDataDownloader: IRemoteFoodDataDownloader = OpenFoodDataDownloader()

    init {
        remoteFoodDataDownloader.addDelegate(this)
    }

    init {
        this.daemonThread.start()
    }

    override fun connect(context: Context) {
        val method = this.javaClass.getDeclaredMethod("connectPvt", Context::class.java )
        val callOp = MethodCall(method, arrayOf(context as Object), this)
        daemonThread.addOperation(call = callOp)
    }


    private fun connectPvt(context: Context) {
        dbHelper = FoodReaderDbHelper(context, false)
        database = dbHelper?.writableDatabase
        notifyDelegates {
            del ->
            del.dbConnectedSuccessfully(this)
        }
    }

    override fun addDelegate(delegate: IDatabaseDelegate) {
        if(this.delegates==null){
            this.delegates = mutableListOf()
        }
        this.delegates?.let {
            list ->
            list.add(delegate)
        }
    }

    private fun notifyDelegates( operation: Consumer<IDatabaseDelegate>){
        if (this.delegates!=null){
            for( delegate in this.delegates!! ){
                operation.accept(delegate)
            }
        }
    }

    override fun deleteDatabase() {
        val method = this.javaClass.getDeclaredMethod("deleteDatabasePvt" )
        val callOp = MethodCall(method, arrayOf(), this)
        daemonThread.addOperation(call = callOp)
    }

    fun deleteDatabasePvt() {
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
        val method = this.javaClass.getDeclaredMethod("getFoodEntriesPvt" )
        val callOp = MethodCall(method, arrayOf(), this)
        daemonThread.addOperation(call = callOp)
    }

    private fun getFoodEntriesPvt() {

        if (this.database==null){
            return
        }
        val projection = arrayOf(BaseColumns._ID, FoodDB.FoodEntry.COLUMN_NAME, FoodDB.FoodEntry.COLUMN_CALORIES
        , FoodDB.FoodEntry.COLUMN_PROTEIN, FoodDB.FoodEntry.COLUMN_FAT, FoodDB.FoodEntry.COLUMN_ALCOL
        ,FoodDB.FoodEntry.COLUMN_SALT, FoodDB.FoodEntry.COLUMN_CARBO, FoodDB.FoodEntry.COLUMN_IS_VEGAN)
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
                val isVegan = cursor.getInt(cursor.getColumnIndexOrThrow(FoodDB.FoodEntry.COLUMN_IS_VEGAN))
                val food = Food(name = name, carbo = carbo, kcal = kcal, protein = proteins, fat = fat
                , alcol = alcol, salt = salt, isVegan = isVegan.toBoolean())
                result.add(food)
            }
        }
        cursor.close()
        notifyDelegates {
            delegate -> delegate.foodDataRetrieved(this, result)
        }
    }

    override fun addFood(food: Food) {
        val method = this.javaClass.getMethod("addFoodPvt", *arrayOf( food::class.java ) )
        val callOp = MethodCall(method, arrayOf(food as Object), this)
        daemonThread.addOperation(call = callOp)
    }

    override fun downloadDataFromInternet() {
        val method = this.javaClass.getMethod("downloadDataFromInternetPvt" )
        val callOp = MethodCall(method, arrayOf(), this)
        daemonThread.addOperation(call = callOp)
    }

    fun downloadDataFromInternetPvt() {
        this.remoteFoodDataDownloader.downloadFoodData()
    }

    fun addFoodPvt(food: Food) {
        var exception: Throwable? = null
        try{
            DbUtility.addFood(this.database!!, food)
            delegates?.forEach{
                it.foodAdded(this, food)
            }
        }catch (e: Throwable){
            delegates?.forEach{
                it.errorRaised(this, e)
            }
        }
    }

    override fun addFoodRelation(foodRelation: FoodRelation) {
        TODO("Not yet implemented")
    }

    override fun foodDownloaded(downloader: IRemoteFoodDataDownloader, foods: Collection<Food>) {
        val failedFoodStore = mutableSetOf<String>()
        for(food in foods){
            this.database?.let {
                val result = DbUtility.addFood(it, food)
                if(result<0){
                    failedFoodStore.add(food.name)
                }
            }
        }
        if(!failedFoodStore.isEmpty()){
            delegates?.forEach {
                it.errorRaised(this, FoodSavingException(failedFoodStore))
            }
        }
        this.getFoodEntries()
    }

    override fun errorRaised(downloader: IRemoteFoodDataDownloader, exception: Throwable) {
        delegates?.forEach{
            it.errorRaised(this, exception)
        }
    }


}
