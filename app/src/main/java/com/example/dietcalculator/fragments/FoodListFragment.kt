package com.example.dietcalculator.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.core.view.iterator
import com.example.dietcalculator.R
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.databinding.FoodListFragmentBinding
import com.example.dietcalculator.dbentities.DbEntity
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.utility.AppConstants
import com.example.dietcalculator.utility.FragmentVisibleDelegate
import com.example.dietcalculator.utility.Utility
import org.json.JSONArray
import java.util.function.Predicate

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FoodListFragment(private val connector: IDatabaseConnector) : Fragment(), IDatabaseDelegate, FoodAdapterListener, FragmentVisibleDelegate {

    companion object {
        val FOOD_LIST_DATA_KEY = "FOOD_LIST_DATA"
    }

    private var stringCondition: StringCondition = StringCondition()
    private var veganCondition: Predicate<Food> = Predicate { food -> food.isVegan }
    private var proteinCondition: Predicate<Food> = Predicate { food -> ((food.protein*4.0)/food.kcal)>AppConstants.PROTEIN_PERCENTAGE }


    private lateinit var foodAdapter: FoodAdapter
    private var foodFilter: FoodFilter = FoodFilter()
    private var initialized = false



    fun setStringFoodFilter(s : String?) {
        this.stringCondition.setInputString(s)
        this.foodAdapter.notifyDataSetChanged()
    }

    class QueryTextListener(private val fragment: FoodListFragment) : SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(s: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(s: String?): Boolean {
            fragment.setStringFoodFilter(s)
            return true
        }

    }

    override fun onPause() {
        super.onPause()
        this.connector.removeDelegate(this)
        this.foodAdapter.removeDelegate(this)
    }


    private var _binding: FoodListFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FoodListFragmentBinding.inflate(inflater, container, false)
        binding.foodListSearchView.setOnQueryTextListener(QueryTextListener(this))
        binding.placeholderView.showView(R.id.no_food_text_view, this.activity)
        binding.floatingUpdateButton.setOnClickListener{
            view ->
            Toast.makeText(this.context, R.string.downloading_msg, Toast.LENGTH_SHORT).show()
            this.foodAdapter.clearList()
            this.connector.getFoodEntries()
        }
        binding.isProteinFood.setOnCheckedChangeListener{
            b, v ->
            if(v){
                this.foodFilter.addCondition(proteinCondition)
            }
            else{
                this.foodFilter.removeCondition(proteinCondition)
            }
            this.foodAdapter.notifyDataSetChanged()
        }
        binding.isVeganSwitch.setOnCheckedChangeListener{
                b, v ->
            if(v){
                this.foodFilter.addCondition(veganCondition)
            }
            else{
                this.foodFilter.removeCondition(veganCondition)
            }
            this.foodAdapter.notifyDataSetChanged()
        }
        this.foodAdapter = FoodAdapter(this.requireContext(), this.foodFilter, this)
        this.foodAdapter.addDelegate(this)
        this.binding.foodListView.adapter = foodAdapter
        this.foodFilter.addNameCondition(stringCondition)
        this.connector.addDelegate(this.foodAdapter)
        this.connector.getFoodEntries()
        this.initialized = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonVal = savedInstanceState?.getString(FOOD_LIST_DATA_KEY)
        jsonVal?.let {
            val jsonArray = JSONArray(jsonVal)
            val lastFoodList = Utility.jsonArrayToFoodList(jsonArray).toMutableList()
            this.foodAdapter.clearList()
            for(food in lastFoodList){
                this.foodAdapter.addFood(food)
            }
        }
        if( this.foodAdapter.isEmptyList() ){
            this.connector.getFoodEntries()
        }
        this.foodAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.foodAdapter.removeDelegate(this)
        _binding = null
    }


    override fun onDetach() {
        super.onDetach()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val jsonArray = JSONArray(this.foodAdapter.getList())
        outState.putString(FOOD_LIST_DATA_KEY, jsonArray.toString(4))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }




    override fun dbConnectedSuccessfully(connector: IDatabaseConnector) {

    }

    override fun onItemsRetrieved(
        connector: IDatabaseConnector,
        type: Class<out DbEntity>,
        count: Int
    ) {
        if(type == Food::class.java){
            this.activity?.runOnUiThread{
                this.foodAdapter.notifyDataSetChanged()
            }
        }
    }




    override fun onItemRetrieved(
        connector: IDatabaseConnector,
        item: DbEntity,
        downloaded: Int?,
        toDownload: Int?
    ) {

    }

    override fun onResume() {
        super.onResume()
        val view = if(this.foodAdapter.hasShowable()) R.id.food_list_view else R.id.no_food_text_view
        this.binding.placeholderView.showView(view, this.activity)
    }


    override fun onItemAddedToDb(
        connector: IDatabaseConnector,
        item: DbEntity,
        downloaded: Int?,
        toDownload: Int?
    ) {

    }

    override fun dbDeleted(connector: IDatabaseConnector) {
        this.binding.placeholderView.showView(R.id.no_food_text_view, this.activity)
    }

    override fun onDBRecreated(connector: IDatabaseConnector) {
        TODO("Not yet implemented")
    }

    override fun errorRaised(connector: IDatabaseConnector, exception: Throwable) {
        this.activity?.runOnUiThread{
            Toast.makeText(this.context, R.string.generic_error, Toast.LENGTH_LONG).show()
        }
        Log.w(AppConstants.APP_LOG_TAG, exception)
    }

    override fun onNoShowableObjects(adapter: FoodAdapter) {
        if( this.initialized ){
            this.binding.placeholderView.showView(R.id.no_food_text_view, this.activity)
        }

    }

    override fun onHasShowableObjects(adapter: FoodAdapter) {
        if( this.initialized ){
            this.binding.placeholderView.showView(R.id.food_list_view, this.activity)
        }
    }

    override fun onGoingToNewFragment(fragment: Fragment) {
        if(!initialized){
            return
        }
        if( this == fragment ){
            this.connector.addDelegate(this)
            this.foodAdapter.notifyDataSetChanged()
        }
        else {
            this.connector.removeDelegate(this)
            this.foodAdapter.removeDelegate(this)
        }
    }

}

fun ViewSwitcher.showView(viewId: Int, activity: Activity?){
    this.reset()
    var iterator = this.iterator()
    var targetView: View? = null
    while( iterator.hasNext() ){
        var nextView = iterator.next()
        if( nextView.id == viewId ){
            targetView = nextView
            break
        }
    }
    targetView?.let {
        activity?.runOnUiThread{
            while( this.nextView != targetView ){
                this.showNext()
            }
            this.showNext()
        }
    }
}