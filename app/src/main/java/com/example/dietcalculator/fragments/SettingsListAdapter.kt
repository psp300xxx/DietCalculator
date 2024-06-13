package com.example.dietcalculator.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.example.dietcalculator.R
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.IDatabaseDelegate
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloader
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloaderDelegate
import com.example.dietcalculator.model.Food
import com.example.dietcalculator.model.FoodRelation
import com.example.dietcalculator.utility.FragmentVisibleDelegate
import com.example.dietcalculator.utility.Utility

class SettingsListAdapter(private val fragment: Fragment,private val context: Context, private val connector:IDatabaseConnector, private val activity: ComponentActivity): BaseAdapter(), IRemoteFoodDataDownloaderDelegate, FragmentVisibleDelegate {

    private val rowMap: MutableMap<Int, Boolean>
    private var progressBar: ProgressBar? = null


    init {
        rowMap = mutableMapOf<Int, Boolean>()
        rowMap.put(R.layout.download_data_button, true)
        rowMap.put(R.layout.download_bar, false)
    }

    private fun getFilteredMap(): Map<Int, Boolean>{
        return rowMap.filter {
            it.value
        }
    }

    private fun getIDList(): List<Int>{
        return rowMap.keys.toList()
    }


    override fun getCount(): Int {
        return getFilteredMap().size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private fun loadView(view: View, index: Int): View{
        when(index){
            0 ->
                return loadDownloadView(view)
            1 ->
                return loadBarView(view)
        }
        return view
    }

    private fun loadBarView(view: View): View {
        this.progressBar = view.findViewById<ProgressBar>(R.id.download_progress_bar)
        progressBar?.max = 1000
        progressBar?.progress = 0
        return view
    }

    private fun loadDownloadView(view: View): View {
        val button = view.findViewById<Button>(R.id.downloadButton)
        button.setOnClickListener{
            this.rowMap[R.layout.download_bar] = true
            this.connector.addDownloadDelegate(this)
            this.connector.downloadDataFromInternet(deleteDb = true)
            this.notifyDataSetChanged()
        }
        return view
    }

    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        var convertView = view
        val viewId = getIDList()[index]
        convertView = LayoutInflater.from(context).inflate(viewId, viewGroup, false);
        convertView = loadView(convertView, index)
        return convertView!!
    }






    override fun onGoingToNewFragment(mFragment: Fragment) {
        if ( this.fragment == mFragment ){
            this.connector.addDownloadDelegate(this)
        }
        else {
            this.connector.removeDownloadDelegate(this)
        }
    }

    override fun onFoodChunckDownloaded(
        downloader: IRemoteFoodDataDownloader,
        foods: Collection<Food>,
        downloaded: Int,
        target: Int
    ) {
        Utility.letIfAllNotNull(this.progressBar) { list: List<Any> ->
            val progressBar = list[0] as ProgressBar
            activity.runOnUiThread {
                progressBar.max = target
                progressBar.progress = downloaded
            }
        }
    }



    override fun errorRaised(downloader: IRemoteFoodDataDownloader, exception: Throwable) {
    }

    override fun onDownloadCompleted(
        downloader: IRemoteFoodDataDownloader,
        partially: Boolean,
        downloadedFood: Int
    ) {
        this.rowMap[R.layout.download_bar] = false
        this.activity.runOnUiThread{
            val msg = if(!partially) R.string.download_complete_text else R.string.download_partially_text
            Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
            this.notifyDataSetChanged()
        }
    }

    override fun onDownloadInterrupted(downloader: IRemoteFoodDataDownloader) {
        this.rowMap[R.layout.download_bar] = false
        this.activity.runOnUiThread{
            this.notifyDataSetChanged()
        }
    }
}