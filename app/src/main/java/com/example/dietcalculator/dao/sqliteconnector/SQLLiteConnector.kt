package com.example.dietcalculator.dao.sqliteconnector

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloader
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloaderDelegate
import com.example.dietcalculator.dao.fooddatadownloader.OpenFoodDataDownloader
import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.dbentities.DbUtility
import com.example.dietcalculator.dbentities.FoodDB
import com.example.dietcalculator.dbentities.FoodReaderDbHelper
import com.example.dietcalculator.dbentities.FoodSavingException
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.utility.Utility.fromMap
import com.example.dietcalculator.utility.Utility.getAnyMap


class SQLLiteConnector() : IDatabaseConnector, IRemoteFoodDataDownloaderDelegate {


    var database: SQLiteDatabase? = null
    var delegates: MutableSet<IDatabaseDelegate>? = null
    var dbHelper: FoodReaderDbHelper? = null
    private val daemonThread: SQLiteThread = SQLiteThread()
    private val remoteFoodDataDownloader: IRemoteFoodDataDownloader = OpenFoodDataDownloader()
    private val downloaderThread = DownloaderThread()

    init {
        remoteFoodDataDownloader.addDelegate(this)
    }

    init {
        this.daemonThread.start()
        this.downloaderThread.start()
    }

    override fun connect(context: Context) {
        val method = this.javaClass.getDeclaredMethod("connectPvt", Context::class.java )
        val callOp = MethodCall(method, arrayOf(context as Object), this)
        daemonThread.addOperation(call = callOp)
    }


    private fun connectPvt(context: Context) {
        dbHelper = FoodReaderDbHelper(context, false)
        database = dbHelper?.writableDatabase
        this.delegates?.forEach{
                del ->
            del.dbConnectedSuccessfully(this)
        }
    }

    override fun addDelegate(delegate: IDatabaseDelegate) {
        if(this.delegates==null){
            this.delegates = mutableSetOf()
        }
        this.delegates?.let {
            list ->
            list.add(delegate)
        }
    }

    override fun removeDelegate(delegate: IDatabaseDelegate) {
        this.delegates?.remove(delegate)
    }

    override fun deleteDatabase(parallelize: Boolean) {
        if(!parallelize){
            deleteDatabasePvt()
            return
        }
        val method = this.javaClass.getDeclaredMethod("deleteDatabasePvt" )
        val callOp = MethodCall(method, arrayOf(), this)
        daemonThread.addOperation(call = callOp)
    }

    override fun recreateDb(parallelize: Boolean){
        if(parallelize){
            val method = this.javaClass.getDeclaredMethod("recreateDbPvt", Context::class.java )
            val callOp = MethodCall(method, arrayOf(), this)
            daemonThread.addOperation(call = callOp)
        }
        else {
            recreateDbPvt()
        }
    }

    private fun recreateDbPvt(){
        deleteDatabasePvt()
        createDbTablePvt()
    }

    private fun createDbTablePvt(){
        try {
            this.database?.let {
                DbUtility.createDatabase(it)
                this.delegates?.forEach{
                    it.onDBRecreated(this)
                }
            }
        }catch (e: Throwable){
            this.delegates?.forEach{
                it.errorRaised(this, e)
            }
        }
    }

    fun deleteDatabasePvt() {
        try {
            this.database?.let {
                DbUtility.deleteDatabase(it)
                this.delegates?.forEach{
                    it.dbDeleted(this)
                }
            }
        }catch (e: Throwable){
            this.delegates?.forEach{
                it.errorRaised(this, e)
            }
        }
    }

    override fun getRelationEntriesFoodInput(list: List<Food>) {
        TODO("Not yet implemented")
    }

    override fun getRelationEntriesNameInputs(list: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getRelationEntriesIDinputs(list: List<Int>) {
        TODO("Not yet implemented")
    }


    override fun getFoodEntries() {
        val method = this.javaClass.getDeclaredMethod("getFoodEntriesPvt" )
        val callOp = MethodCall(method, arrayOf(), this)
        daemonThread.addOperation(call = callOp)
    }

    private fun getItemsFromDb(entityClass: Class<out DbEntity>){
        if (this.database==null){
            return
        }
        val entity = entityClass.newInstance()
        var cursorNullable: Cursor? = null
        try {
            val projection = entity.getColumns()
            database?.let {
                var cursor = it.query(
                    FoodDB.FoodEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
                )
                cursorNullable = cursor
                var count = 0
                while(cursor.moveToNext()){
                    val map = cursor.getAnyMap(projection)
                    val item = fromMap(map, entity.javaClass)
                    this.delegates?.forEach{
                            d -> d.onItemRetrieved(this, item)
                    }
                    count+=1
                }
                this.delegates?.forEach{
                        d -> d.onItemsRetrieved(this, entityClass,count)
                }
            }
        }catch (e: Exception){
            this.delegates?.forEach{
                it.errorRaised(this, e)
            }
        }
        finally {
            cursorNullable?.close()
        }
    }

    private fun getFoodEntriesPvt() {
        getItemsFromDb(Food::class.java)
    }

    override fun addFood(food: Food) {
        val method = this.javaClass.getMethod("addItemPvt", *arrayOf( food::class.java ) )
        val callOp = MethodCall(method, arrayOf(food as Object), this)
        daemonThread.addOperation(call = callOp)
    }

    override fun downloadDataFromInternet(deleteDb: Boolean) {
        val db = if(deleteDb)this else null
        downloaderThread.triggerDownload(this.remoteFoodDataDownloader, db)
    }


    fun addItemPvt(item: DbEntity){
        var exception: Throwable? = null
        try{
            this.database?.let {
                DbUtility.addRow(it, item)
            }
            delegates?.forEach{
                it.onItemAddedToDb(this, item)
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



    override fun onFoodChunckDownloaded(
        downloader: IRemoteFoodDataDownloader,
        foods: Collection<Food>,
        downloaded: Int,
        target: Int
    ) {
        val failedFoodStore = mutableSetOf<String>()
        for(food in foods){
            this.database?.let {
                val result = DbUtility.addRow(it, food)
                if(result<0){
                    failedFoodStore.add(food.name)
                }
                else{
                    delegates?.forEach {
                        it.onItemAddedToDb(this, food, downloaded, target)
                    }
                }
            }
        }
        if(!failedFoodStore.isEmpty()){
            delegates?.forEach {
                it.errorRaised(this, FoodSavingException(failedFoodStore))
            }
        }
    }

    override fun errorRaised(downloader: IRemoteFoodDataDownloader, exception: Throwable) {

        delegates?.forEach{
            it.errorRaised(this, exception)
        }
    }

    override fun addDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate) {
        this.remoteFoodDataDownloader.addDelegate(d)
    }

    override fun removeDownloadDelegate(d: IRemoteFoodDataDownloaderDelegate) {
        this.remoteFoodDataDownloader.removeDelegate(d)
    }

    override fun onDownloadCompleted(
        downloader: IRemoteFoodDataDownloader,
        partially: Boolean,
        downloadedFood: Int
    ) {

    }

    override fun onDownloadInterrupted(downloader: IRemoteFoodDataDownloader) {

    }


}
