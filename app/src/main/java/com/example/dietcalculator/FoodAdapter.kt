package com.example.dietcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.dietcalculator.model.Food

class FoodAdapter(private val list: List<Food>, private val context: Context): BaseAdapter() {


    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): Food {
        return list[index]
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
}