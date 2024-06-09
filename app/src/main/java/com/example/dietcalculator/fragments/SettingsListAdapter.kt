package com.example.dietcalculator.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Toast
import com.example.dietcalculator.R
import com.example.dietcalculator.dao.IDatabaseConnector

class SettingsListAdapter(private val context: Context, private val connector:IDatabaseConnector): BaseAdapter() {
    override fun getCount(): Int {
        return 1
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private fun loadDownloadView(view: View): View {
        val button = view.findViewById<Button>(R.id.downloadButton)
        button.setOnClickListener{
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            connector.downloadDataFromInternet()
        }
        return view
    }

    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        var convertView = view
        if(index==0){
            convertView = LayoutInflater.from(context).inflate(R.layout.download_data_button, viewGroup, false);
            convertView = loadDownloadView(convertView)
        }
        return convertView!!
    }
}