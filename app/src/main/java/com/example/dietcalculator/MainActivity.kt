package com.example.dietcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.SQLLiteConnector
import com.example.dietcalculator.databinding.ActivityMainBinding
import com.example.dietcalculator.model.Food
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), IDatabaseDelegate {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbConnector: IDatabaseConnector
    private lateinit var substituteCalculatorFragment: SubstituteCalculatorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val navigationView = this.findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnItemSelectedListener { item -> this.onOptionsItemSelected(item) }
        substituteCalculatorFragment = SubstituteCalculatorFragment()
        dbConnector = SQLLiteConnector()
        dbConnector.addDelegate(this)
        dbConnector.connect(this.baseContext)
//        setCurrentFragment(firstFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_view, fragment)
            commit()
        }

    // Handling the click events of the menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Switching on the item id of the menu item
        when (item.itemId) {
            R.id.substitute_tab_button -> {
                this.setCurrentFragment(substituteCalculatorFragment)
                return true
            }
            R.id.settings_tab_button -> {
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "Menu "+item.title+" is Pressed", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.food_view_tab_button -> {
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "Menu "+item.title+" is Pressed", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun dbConnectedSuccessfully(connector: IDatabaseConnector) {
        Toast.makeText(this,"Successfully connected to DB", Toast.LENGTH_LONG)
    }

    override fun foodDataRetrieved(connector: IDatabaseConnector, list: List<Food>) {
        val sb : StringBuilder = StringBuilder()
        for ( food in list ){
            sb.append(food.toString() + ", ")
        }
        sb.setLength(sb.length-2)
        Toast.makeText(this,sb, Toast.LENGTH_LONG).show()
    }

    override fun errorRaised(connector: IDatabaseConnector, exception: Throwable) {
        Toast.makeText(this,exception.message, Toast.LENGTH_LONG).show()
    }

    override fun foodAdded(connector: IDatabaseConnector, food: Food) {
        Toast.makeText(this,food.toString() + " added to database", Toast.LENGTH_LONG).show()
    }

    override fun dbDeleted(connector: IDatabaseConnector) {
        Toast.makeText(this,"database deleted successfully", Toast.LENGTH_LONG).show()
    }
}