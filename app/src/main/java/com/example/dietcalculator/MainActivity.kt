package com.example.dietcalculator

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.SQLLiteConnector
import com.example.dietcalculator.databinding.ActivityMainBinding
import com.example.dietcalculator.dbentities.DbUtility
import com.example.dietcalculator.dbentities.FoodDB
import com.example.dietcalculator.dbentities.FoodReaderDbHelper
import com.example.dietcalculator.model.Food


class MainActivity : AppCompatActivity(), IDatabaseDelegate {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SQLiteDatabase
    private lateinit var dbConnector: IDatabaseConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            this.dbConnector.getFoodEntries()
        }
        dbConnector = SQLLiteConnector()
        dbConnector.addDelegate(this)
        dbConnector.connect(this.baseContext)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        dbConnector.getFoodEntries()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
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
        TODO("Not yet implemented")
    }
}