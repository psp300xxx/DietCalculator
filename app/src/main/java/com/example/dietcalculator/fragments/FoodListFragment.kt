package com.example.dietcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.example.dietcalculator.FoodAdapter
import com.example.dietcalculator.R
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.databinding.FragmentSecondBinding
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FoodListFragment(private val connector: IDatabaseConnector) : Fragment(), IDatabaseDelegate {

    private var foodFilter : String? = null

    private var foodAdapter: FoodAdapter? = null

    private var lastFoodList: List<Food>? = null

    fun setFoodFilter(s : String?) {
        this.foodFilter = s
    }

    class QueryTextListener(private val fragment: FoodListFragment) : SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(s: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(s: String?): Boolean {
            s?.let {
                s ->
                fragment.setFoodFilter(s)
            }
            if (s==null || s.isEmpty()){
                fragment.setFoodFilter(null)
            }
            fragment.lastFoodList?.let {
                list ->
                fragment.foodDataRetrieved(fragment.connector, list)
            }
            return true
        }

    }

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        binding.foodListSearchView.setOnQueryTextListener(QueryTextListener(this))
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
        connector.getFoodEntries()
    }



    override fun dbConnectedSuccessfully(connector: IDatabaseConnector) {

    }

    override fun foodDataRetrieved(connector: IDatabaseConnector, list: List<Food>) {
        this.lastFoodList = list
        val filteredList = this.lastFoodList?.filter {
            food ->
            if(this.foodFilter==null){
                true
            }
            else{
                var result = false
                this.foodFilter?.let {
                    filter ->
                    result = food.name.uppercase().contains(filter.uppercase())
                }
                result
            }
        }
        filteredList?.let {
            this.activity?.runOnUiThread {
                this.binding.foodListView.visibility = View.VISIBLE
                this.binding.noFoodTextView.visibility = View.INVISIBLE
                this.foodAdapter = FoodAdapter(filteredList, this.requireContext())
                this.binding.foodListView.adapter = foodAdapter
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
    }
}