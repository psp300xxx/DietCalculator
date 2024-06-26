package com.example.dietcalculator.fragments

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
import com.example.dietcalculator.dao.mockconnector.MockConnector
import com.example.dietcalculator.databinding.FoodListFragmentBinding
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.utility.AppConstants
import java.util.function.Predicate
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FoodListFragment(private val connector: IDatabaseConnector) : Fragment(), IDatabaseDelegate {

    private var stringCondition: StringCondition = StringCondition()
    private var veganCondition: Predicate<Food> = Predicate { food -> food.isVegan }
    private var proteinCondition: Predicate<Food> = Predicate { food -> ((food.protein*4.0)/food.kcal)>AppConstants.PROTEIN_PERCENTAGE }


    private var foodAdapter: FoodAdapter? = null

    private var lastFoodList: List<Food>? = null
    private var filterActiveMap: MutableMap<Predicate<Food>, Boolean> = mutableMapOf()


    fun setStringFoodFilter(s : String?) {
        this.stringCondition.setInputString(s)
    }

    class QueryTextListener(private val fragment: FoodListFragment) : SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(s: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(s: String?): Boolean {
            s?.let {
                s ->
                fragment.setStringFoodFilter(s)
            }
            if (s==null || s.isEmpty()){
                fragment.setStringFoodFilter(null)
            }
            fragment.lastFoodList?.let {
                list ->
                fragment.foodDataRetrieved(fragment.connector, list)
            }
            return true
        }

    }

    override fun onPause() {
        super.onPause()
        this.connector.removeDelegate(this)
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
        binding.placeholderView.showView(R.id.no_food_text_view)
        binding.floatingActionButton.setOnClickListener {
            view ->
            this.addButtonPressedMockConnector()
        }

        binding.floatingUpdateButton.setOnClickListener{
            view ->
            Toast.makeText(this.context, R.string.downloading_msg, Toast.LENGTH_SHORT).show()
            this.connector.downloadDataFromInternet()
        }
        binding.isProteinFood.setOnCheckedChangeListener{
            b, v ->
            filterActiveMap[proteinCondition] = v
            this.lastFoodList?.let {
                this.foodDataRetrieved(this.connector, it)
            }
        }
        binding.isVeganSwitch.setOnCheckedChangeListener{
                b, v ->
            filterActiveMap[veganCondition] = v
            this.lastFoodList?.let {
                this.foodDataRetrieved(this.connector, it)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connector.getFoodEntries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        this.connector.addDelegate(this)
        this.connector.getFoodEntries()
    }

    fun addButtonPressedMockConnector(){
        if( this.connector is MockConnector ){
            val c = this.connector as MockConnector
            if ( c.foodNumber() == 0 ){
                c.createFoods(Random.nextInt(20))
            }
            else {
                c.clearDb()
            }
        }
    }



    override fun dbConnectedSuccessfully(connector: IDatabaseConnector) {

    }

    override fun foodDataRetrieved(connector: IDatabaseConnector, list: List<Food>) {
        this.lastFoodList = list
        val foodFilter = FoodFilter()
        foodFilter.addNameCondition(stringCondition)
        if(this.filterActiveMap[veganCondition] == true){
            foodFilter.addCondition(veganCondition)
        }
        if(this.filterActiveMap[proteinCondition] == true){
            foodFilter.addCondition(proteinCondition)
        }
        val filteredList = this.lastFoodList?.filter {
            food ->
            foodFilter.isMatching(food)
        }
        filteredList?.let {
            val hasFoods = !filteredList.isEmpty()
            val viewToShow = if(hasFoods) R.id.food_list_view else R.id.no_food_text_view
            this.activity?.runOnUiThread {
                if (hasFoods){
                    this.foodAdapter = FoodAdapter(filteredList, this.requireContext())
                    this.binding.foodListView.adapter = foodAdapter
                }
                this.binding.placeholderView.showView(viewToShow)
            }
        }
    }

    override fun foodAdded(connector: IDatabaseConnector, food: Food) {
        connector.getFoodEntries()
    }

    override fun dbDeleted(connector: IDatabaseConnector) {
        this.activity?.runOnUiThread {
            this.binding.foodListView.visibility = View.INVISIBLE
            this.binding.noFoodTextView.visibility = View.VISIBLE
        }
    }

    override fun foodRelationAdded(connector: IDatabaseConnector, foodRelation: FoodRelation) {
        TODO("Not yet implemented")
    }

    override fun allFoodRelationsRetrieved(
        connector: IDatabaseConnector,
        relations: List<FoodRelation>
    ) {
        TODO("Not yet implemented")
    }


    override fun filteredFoodRelationsRetrieved(
        connector: IDatabaseConnector,
        relations: List<FoodRelation>,
        foods: List<Food>
    ) {
        TODO("Not yet implemented")
    }

    override fun errorRaised(connector: IDatabaseConnector, exception: Throwable) {
        this.activity?.runOnUiThread{
            Toast.makeText(this.context, R.string.generic_error, Toast.LENGTH_LONG).show()
        }
        Log.w(AppConstants.APP_LOG_TAG, exception)
    }
}

fun ViewSwitcher.showView(viewId: Int){
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
        while( this.nextView != targetView ){
            this.showNext()
        }
        this.showNext()
    }
}