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
import com.example.dietcalculator.dao.mockconnector.MockConnector
import com.example.dietcalculator.dao.sqliteconnector.SQLLiteConnector
import com.example.dietcalculator.databinding.ActivityMainBinding
import com.example.dietcalculator.fragments.FoodListFragment
import com.example.dietcalculator.fragments.SettingsFragment
import com.example.dietcalculator.fragments.SubstituteCalculatorFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbConnector: IDatabaseConnector
    private lateinit var substituteCalculatorFragment: SubstituteCalculatorFragment
    private lateinit var foodListFragment: FoodListFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val navigationView = this.findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnItemSelectedListener { item -> this.onOptionsItemSelected(item) }
        substituteCalculatorFragment = SubstituteCalculatorFragment()
        dbConnector = SQLLiteConnector()
//        dbConnector = MockConnector()
        dbConnector.connect(this.baseContext)
        foodListFragment = FoodListFragment(connector = dbConnector)
        settingsFragment = SettingsFragment(connector = dbConnector)
        dbConnector.addDelegate(foodListFragment)
        setCurrentFragment(substituteCalculatorFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
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
                this.setCurrentFragment(settingsFragment)
                return true
            }
            R.id.food_view_tab_button -> {
                // Code to be executed when the add button is clicked
                this.setCurrentFragment(foodListFragment)
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



}