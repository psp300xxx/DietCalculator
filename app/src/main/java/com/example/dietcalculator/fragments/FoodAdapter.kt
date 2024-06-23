package com.example.dietcalculator.fragments

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dietcalculator.R
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import java.util.concurrent.locks.ReentrantLock

class FoodAdapter(private val context: Context, private val filter: FoodFilter, private val fragment: Fragment?): BaseAdapter(), IDatabaseDelegate {
    private val delegates: MutableList<FoodAdapterListener> = mutableListOf()
    private var list: MutableList<Food> = mutableListOf()
    private var hasShowable: Boolean? = null
    private val lock = ReentrantLock()

    private val activity: Activity?
        get() = this.fragment?.activity

    companion object {
        val MAX_SHOWABLE = 100
    }

    fun addFood(food: Food){
        lock.lock()
        try {
            this.activity?.runOnUiThread{
                this.list.add(food)
                this.notifyDataSetChanged()
            }
        }finally {
            lock.unlock()
        }
    }

    fun clearList(){
        lock.lock()
        try {
            this.activity?.runOnUiThread{
                this.list.clear()
                this.notifyDataSetChanged()
            }
        }finally {
            lock.unlock()
        }
    }

    fun isEmptyList():Boolean{
        return this.list.isEmpty()
    }

    fun getList(): List<Food>{
        return this.list.toList()
    }

    fun hasShowable(): Boolean{
        var r = false
        this.hasShowable?.let {
        v->
            r = v
        }
        return r
    }

    override fun getCount(): Int {
            val objects = list.getFilteredList(filter).size
            val newHasShowable = objects!=0
            if( newHasShowable != hasShowable ){
                this.hasShowable = newHasShowable
                if(this.hasShowable!!){
                    this.delegates.forEach {
                        it.onHasShowableObjects(this)
                    }
                }
                else {
                    this.delegates.forEach {
                        it.onNoShowableObjects(this)
                    }
                }
            }
            return Math.min(MAX_SHOWABLE, objects)
    }

    fun addDelegate(d: FoodAdapterListener){
        this.delegates.add(d)
    }

    fun removeDelegate(d: FoodAdapterListener){
        this.delegates.remove(d)
    }

    override fun getItem(index: Int): Food {
        return list.getFilteredList(filter)[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getView(index: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_view, viewGroup, false);
        }
        val currentItem = getItem(index) as Food
        val foodNameTextView = convertView
            ?.findViewById(R.id.food_name_text_view) as TextView
        val foodCaloriesTextView = convertView
            ?.findViewById(R.id.food_calories_text_view) as TextView
        foodNameTextView.text = currentItem.name
        foodCaloriesTextView.text = String.format( "%.2f kcal for 100g" ,currentItem.kcal)
        return convertView
    }

    override fun dbConnectedSuccessfully(connector: IDatabaseConnector) {

    }

    override fun onItemsRetrieved(
        connector: IDatabaseConnector,
        type: Class<out DbEntity>,
        count: Int
    ) {

    }




    override fun onItemRetrieved(
        connector: IDatabaseConnector,
        item: DbEntity,
        downloaded: Int?,
        toDownload: Int?
    ) {
        if (item is Food){
            this.addFood(item)
        }
    }



    override fun onItemAddedToDb(
        connector: IDatabaseConnector,
        item: DbEntity,
        downloaded: Int?,
        toDownload: Int?
    ) {
        TODO("Not yet implemented")
    }

    override fun dbDeleted(connector: IDatabaseConnector) {

    }

    override fun onDBRecreated(connector: IDatabaseConnector) {

    }




    override fun errorRaised(connector: IDatabaseConnector, exception: Throwable) {
        this.fragment?.let {
            if( fragment is FoodListFragment ){
                fragment.errorRaised(connector, exception)
            }
        }
    }
}

fun List<Food>.getFilteredList(filter: FoodFilter): List<Food> {
    return this.filter { food -> filter.isMatching(food) }
}